package me.arcticlight.animations
import scala.language.implicitConversions

object ScalaTween {
  trait TweenOps[T <: TweenOps[T]]  {
    /**
     * Scalar multiply (Tween operation) multiplies this value by a fraction and returns the result
     * @param fraction A [[Float]] value between 0 and 1, inclusive
     * @return a T scaled by multiplying it with the scalar `fraction` amount
     */
    def *(fraction: Float): T

    /**
     * Add (Tween operation) adds together this object and the parameter and returns the result
     * @param other Another [[T]] to add to this one
     * @return The result of adding together `this` and `other`
     */
    def +(other: T): T

    /**
     * Subtract (Tween operation) subtracts the parameter from this object and returns the result.
     * @param other Another [[T]] to subtract from this one
     * @return The result of subtracting `other` from `this`
     */
    def -(other: T): T

    /**
     * Perform linear interpolation using TweenOps.
     * @param other the other TweenOps with which to lerp
     */
    def lerp(other: T, fraction: Float): T
      = this * fraction + other * (1-fraction)

    def lease(other: T, fraction: Float, fease: (Float) => Float): T
      = this + (other - this) * fease(fraction)
  }
  implicit def unwrapTweenOps[T <: TweenOps[T]](ops: TweenOps[T]): T = ops
  implicit def detwopsNumeric[A](twops: TwopsyNumeric[A]): A = twops.value

  /**
   * Implicitly adds [[TweenOps]][A] to any `A` with [[Numeric]].
   *
   * This is possible because any typeclass implementing [[Numeric]] should
   * already have `+`, `-`, and `*` operations that [[TweenOps]] can use.
   *
   * @param value the underlying numeric value
   * @tparam A a type extending [[Numeric]]
   * @author Hawk Weisman
   */
  implicit class TwopsyNumeric[A : Numeric](val value: A)
  extends TweenOps[TwopsyNumeric[A]] {

    override def *(fraction: Float): TwopsyNumeric[A]
      = value * fraction
    override def +(other: TwopsyNumeric[A]): TwopsyNumeric[A]
      = value + other
    override def -(other: TwopsyNumeric[A]): TwopsyNumeric[A]
      = value - other

  }
  class AnimationTarget[T <: TweenOps[T]](var value: T) {
  }
}
