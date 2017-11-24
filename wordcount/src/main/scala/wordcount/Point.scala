package wordcount

import scala.Serializable

@SerialVersionUID(121020161L)
abstract class Point(val values: Product) extends Serializable {
  /** Returns a string representation of this object
    *
    * Subclasses could override this method to give a better representation
    */
  override def toString(): String = "point: " + values.toString

}

abstract class PointFunctions[P <: Point](val point: P) {
  
  def distance(that: P): Double
}