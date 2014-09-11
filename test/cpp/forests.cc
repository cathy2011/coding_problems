// copied from Internet.

#include <iostream>
#include <cmath>

using namespace std;

struct POINT {
  int x;
  int y;
};

int main(void) {
  const double fRad = 57.295779513082320876798154814105f;
  const double fMax = 0.99999998476912904932780850903444f;
  for (double d, x, y; cin >> d >> x >> y && d != 0;) {
    POINT Eye = { int(x * 100 + 0.5), int(y * 100 + 0.5) };
    int nDiam = (int) (d * 100 + 0.5), nCnt = 0;
    for (int iBeg = 0, iEnd = 100; iEnd <= 1000; iBeg -= 100, iEnd += 100) {
      for (int i = 0; i < iEnd - iBeg; i += 100) {
        POINT Out[4] = { { iBeg, iBeg + i }, { iBeg + i, iEnd }, { iEnd, iEnd
            - i }, { iEnd - i, iBeg } };

        for (int j = 0; j < 4; j++) {
          POINT In = { iBeg + 100, iBeg + 100 };
          for (; In.y <= iEnd - 100; In.y += 100) {
            for (In.x = iBeg + 100; In.x <= iEnd - 100; In.x += 100) {
              POINT NVec = { In.x - Eye.x, In.y - Eye.y };
              POINT FVec = { Out[j].x - Eye.x, Out[j].y - Eye.y };
              int nProd = NVec.x * FVec.x + NVec.y * FVec.y;
              double fNMod = sqrt((double) (NVec.x * NVec.x + NVec.y * NVec.y));
              double fFMod = sqrt((double) (FVec.x * FVec.x + FVec.y * FVec.y));
              double fACOS = nProd / (fNMod * fFMod);
              if (fACOS >= fMax) {
                break;
              }
              double fAng = acos(fACOS) * fRad;
              fNMod = asin((double) nDiam / 2.0f / fNMod) * fRad;
              fFMod = asin((double) nDiam / 2.0f / fFMod) * fRad;
              if (fAng - fNMod - fFMod <= 0.01f) {
                break;
              }
            }
            if (In.x <= iEnd - 100) {
              break;
            }
          }
          nCnt += (In.y > iEnd - 100);
        }
      }
    }
    cout << nCnt << endl;
  }
  return 0;
}
