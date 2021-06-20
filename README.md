# SBT Project Template 

## A standardized Scala project layout.

This 'modern' [giter8][1] template will write SBT build files for a Scala projects.
Includes common set of plugins, __tut__, __jmh__ etc.


## Usage 

```sbt -Dsbt.version=1.2.8 new lewismj/sbt-project.g8```


## Directory Layout

~~~
+-- name
|  +-- build.sbt
|  +-- name 
|    +-- bench
|    +-- core 
|      +-- src
|        +-- main 
|          +-- scala 
|            +-- package 
|              +-- Placeholder.scala
|              +-- instances
|                +-- all.scala
|              +-- implicits
|                +-- package.scala
|     +-- tests
|       +-- src
|         +-- main
|           +-- scala
|             +-- package
|               +-- tests
|                 +-- arbitrary
|                   +-- AllArbitrary.scala
|         +-- test
|     +-- docs
|  +-- project
|    +-- build.properties
|    +-- plugins.sbt
~~~

## Example

~~~
sbt project

organization [com.example]: com.waioeka
package [package]: weka
name [projectName]: weka
description [sbt project]: dummy project
scala_version [2.12.2]: 
version [0.0.1]: 
gh_name [developer name]: Michael Lewis
gh_username [username]: lewismj
Template applied to .
waiheke:github lewismj$
~~~

## What gets produced ?

The basic outline for tut & microsite documentation is written. Inside 'core' and 'tests',
some standard code scaffolding is added.


### Implicits

~~~
package weka 

object implicits extends instances.AllInstances
~~~

~~~
package weka 
package instances

trait AllInstances
~~~


### Tests

~~~
package weka
package tests

import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline

/**
  * Base definition for Weka test suites.
  */
trait WekaSuite extends FunSuite
  with BeforeAndAfterAll
  with Checkers
  with Matchers
  with GeneratorDrivenPropertyChecks
  with Discipline {
}
~~~


[1]: https://github.com/n8han/giter8
