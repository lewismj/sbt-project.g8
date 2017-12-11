package $package$
package bench

import org.openjdk.jmh.annotations.{Benchmark, Scope, State}

@State(Scope.Benchmark)
class Bench {

  @Benchmark
  def PlaceholderBenchmark(): Int = 1

}
