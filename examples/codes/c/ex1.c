#include <math.h>
int main() {
  unsigned int N = nondet_uint();
  double x = nondet_double();
  if(x <= 0 || isnan(x))
    return 0;
  unsigned int i = 0;
  while(i < N) {
    x = (2*x);
    assert(x>0);
    ++i;
  }
  assert(x>0);
  return 0;
}