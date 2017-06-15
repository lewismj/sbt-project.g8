package $package$
package tests

import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline

/**
  * Base definition for $package;format="Camel"$ test suites.
  */
trait  $package;format="Camel"$Suite extends FunSuite
  with BeforeAndAfterAll
  with Checkers
  with Matchers
  with GeneratorDrivenPropertyChecks
  with Discipline {
}
