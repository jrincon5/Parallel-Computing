package wordcount
//scala collections imports
import scala.collection.immutable.{Map, HashMap, Set, HashSet}


class Document(override val values: (String, HashMap[String, Int]))
    extends Point(values) {
  /** Returns the filename of this document */
  override def toString(): String = "Document: " + this.values._1
}

object Document {

  def apply(name: String, bagOfWords: Map[String, Int]): Document = {
    new Document((name, HashMap.empty[String, Int] ++ bagOfWords))
  }
}

class DocumentFunctions(override val point: Document)
    extends PointFunctions[Document](point) {

  override def distance(that: Document): Double = {
    //import the power and the square root functions
    import scala.math.{pow, sqrt}

    //get the vectors of both documents
    val ta = this.point.values._2
    val tb = that.values._2

    //create a set with all words in both documents
    val words = ta.keySet | tb.keySet

    //compute the dot product between them and the norm of both
    var dotPro = 0.0
    var taNorm = 0.0
    var tbNorm = 0.0
    for (w <- words) {
      val a = ta.getOrElse(w, 0)
      val b = tb.getOrElse(w, 0)
      dotPro += (a * b)
      taNorm += pow(a, 2)
      tbNorm += pow(b, 2)
    }
    taNorm = sqrt(taNorm)
    tbNorm = sqrt(tbNorm)

    //compute the distance as 1 - SIMc(ta, tb)
    //where SIMc is the cosine similarity
    val similarity = dotPro / (taNorm * tbNorm)
    1 - similarity
  }
}