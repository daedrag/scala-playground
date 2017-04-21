// Currying is a transformation which converts
// a function f of two arguments into a function
// of one argument that partially applies f
def curry[A, B, C](f: (A, B) => C): A => (B => C) =
  a => b => f(a, b)

def f(a: Int, b: Int): Int = a + b
curry(f)(1)(4) // should be 5

def g(a: Int)(b: Int): Int = a + b
//curry(g)(1)(4)  // this does not compile