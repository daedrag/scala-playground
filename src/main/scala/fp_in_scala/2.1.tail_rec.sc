def fib(n: Int): Int = {
  @annotation.tailrec
  def loop(n: Int, prev: Int, cur: Int): Int =
    if (n <= 0) prev
    else loop(n - 1, cur, prev + cur)
  loop(n, 0, 1)
}

fib(4)  // should be 3
fib(5)  // should be 5
fib(6)  // should be 8