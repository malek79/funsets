package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }

  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(4)
    val s5 = singletonSet(5)
    val s12 = union(s1, s2)
    val s23 = union(s2, s3)
    val s124 = union(s12, s4)
    val s1234 = union(s124, s3)
    val s123 = union(s12, s3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
      assert(!contains(s1, 4), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      assert(contains(s12, 1), "Union 1")
      assert(contains(s12, 2), "Union 2")
      assert(!contains(s12, 3), "Union 3")
    }
  }

  test("intersect contains elements  common to both sets") {
    new TestSets {

      val si = intersect(s124, s2)
      assert(!contains(si, 1), "Union 1")
      assert(contains(si, 2), "Union 2")
      assert(!contains(si, 3), "Union 3")
    }
  }
  test("diff contains all elements of `s` that are not in `t`") {
    new TestSets {
      val si = diff(s124, s2)
      assert(contains(si, 1), "Union 1")
      assert(!contains(si, 2), "Union 2")
      assert(contains(si, 4), "Union 4")
    }
  }

  test("filter by predicate") {
    new TestSets {

      def p(x: Int) = x < 3
      val filt = filter(s1234, p)
      assert(contains(filt, 1), "filter 1")
      assert(contains(filt, 2), "filter 2")
      assert(!contains(filt, 4), "filter 4")
    }
  }

  test("forall by predicate") {
    new TestSets {
      assert(forall(s123, (x) => x == 1 || x == 2 || x == 3), "forall 1")
      assert(forall(s23, (x) => x > 1), "forall 2")
      assert(!forall(s123, (x) => x > 1), "forall 3")
      assert(!forall(s1, (x) => x > 1), "forall 4")
    }
  }

  test("exists by predicate") {
    new TestSets {
      assert(exists(s123, (x) => x == 1 || x == 2 || x == 3), "exists 1")
      assert(exists(s23, (x) => x > 1), "exists 2")
      assert(exists(s123, (x) => x > 1), "exists 3")
      assert(!exists(s1, (x) => x > 1), "exists 4")
    }
  }

  test("map by predicate") {
    new TestSets {
      def p(x: Int) = x < 3

      assert(!contains(map(s1, (x) => x+1),1), "contains 1")
      assert(contains(map(s123, (x) => x+1),2), "contains 2")
      assert(!contains(map(s12, (x) => x+1),0), "contains 0")
    }
  }
}
