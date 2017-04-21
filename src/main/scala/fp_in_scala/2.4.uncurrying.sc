def uncurry[A, B, C](f: A => B => C): (A, B) => C =
  (a, b) => f(a)(b)

def f(a: Int, b: Int): Int = a + b
def g(a: Int)(b: Int): Int = a + b

uncurry(g)(1, 3)  // should be 4
// uncurry(f)(1, 3)  // this does not compile