package $package$
package tests


/* -- scala 3???
import org.scalacheck.Gen 
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
*/

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalacheck.Gen


/**
  * Base definition for Hello test suites.
  */
trait  HelloSuite extends AnyFunSuite
  with BeforeAndAfterAll
  with Checkers
  with Matchers {}
