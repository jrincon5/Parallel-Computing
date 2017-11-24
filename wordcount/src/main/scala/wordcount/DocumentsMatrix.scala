package wordcount

import org.apache.spark.sql.SparkSession


import scala.collection.immutable.{Map, Set, HashSet}

object DocumentsMatrix {
  
  private def countWords(text: String, stopWords: Set[String]): Map[String, Int] = {
    //checks if a word is valid
    def isValidWord(word: String): Boolean = {
      word != "" && word.length > 3 && !stopWords(word)
    }

    //returns the inflected form of a word
    def toInflectedForm(word: String): String = {
      word.toLowerCase map {
        case 'á' => 'a'
        case 'à' => 'a'
        case 'â' => 'a'
        case 'ä' => 'a'
        case 'é' => 'e'
        case 'è' => 'e'
        case 'ê' => 'e'
        case 'ë' => 'e'
        case 'í' => 'i'
        case 'ì' => 'i'
        case 'î' => 'i'
        case 'ï' => 'i'
        case 'ó' => 'o'
        case 'ò' => 'o'
        case 'ô' => 'o'
        case 'ö' => 'o'
        case 'ú' => 'u'
        case 'ù' => 'u'
        case 'û' => 'u'
        case 'ü' => 'u'
        case c   => c
      }
    }

    val words = for {
      w <- text.split("\\P{L}") //splits by every character that isn't a letter
      word = toInflectedForm(w)
      if isValidWord(word)
    } yield word

    val counts = words groupBy (w => w) mapValues (_.size)
    counts.toMap
  }
  
  def main(args: Array[String]): Unit = {
    //starts the application
    val spark = SparkSession.builder().appName("WordCount").getOrCreate()
    val sc = spark.sparkContext


    //creates the set of stop words
    import scala.io.Source
    val englishStopWordsStream = getClass.getResourceAsStream("/english-stop-words")
    val spanishStopWordsStream = getClass.getResourceAsStream("/spanish-stop-words")
    val englishStopWordsSource = Source.fromInputStream(englishStopWordsStream)
    val spanishStopWordsSource = Source.fromInputStream(spanishStopWordsStream)
    val englishStopWordsSet = englishStopWordsSource.mkString.split(",").toSet
    val spanishStopWordsSet = spanishStopWordsSource.mkString.split(",").toSet
    val stopWords = sc.broadcast(englishStopWordsSet | spanishStopWordsSet)
    englishStopWordsSource.close()
    spanishStopWordsSource.close()

    //opens all documents specified in the first argument
    val documents = sc.wholeTextFiles("/datasets/gutenberg-txt-es/*-8.txt")
    //val wordsPerDocument = sc.textFile("/user/jrincon5/out_wordcount/part-00000");

    //creates a bag of words for each document
    val wordsPerDocument = documents map {
      case (filename, text) =>
        (filename.substring(filename.lastIndexOf("/") + 1),
          countWords(text, stopWords.value))
    }

    //creates a point for each document
    val points = wordsPerDocument map { case(filename, bagOfWords) => Document(filename, bagOfWords) }
    // creates a matrix of the bagOfWords
    val matrix = points.cartesian(points)
    
    // creates the similarity DocumentsMatrix
    val outPut = for {
      index <- matrix
      doc1=index._1
      doc2=index._2
      //castFun = (doc1: Document) => new DocumentFunctions(doc1)
      castFun = new DocumentFunctions(doc1);
      distanc=castFun.distance(doc2)
    } yield (index._1.values._1 , index._2.values._1 , distanc)


    //saves the results
    outPut.saveAsTextFile("/user/jrincon5/out_matrix_similarity")
    sc.stop()
  }
}