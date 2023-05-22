import os
from pyproc import *
procOpts(nprocess=7)
FID(FIDHOME+'bruker/ubiq_noe/6')
CREATE(TMPHOME+'tst_ubiq_noe.nv')
acqOrder('a2','p1','d1')
acqarray(0,2)
fixdsp(True)
skip(0,0)
label('1H','15N')
acqsize(0,0)
tdsize(0,0)
sf('SFO1,1','SFO1,2')
sw('SW_h,1','SW_h,2')
ref('h2o','N')
DIM(1)
TDCOMB(dim=2,coef='echo-antiecho-r')
TDSS(winSize=17)
SB()
ZF()
FT()
PHASE(ph0=147.3,ph1=3.8,dimag=False)
EXTRACT(start=7,end=462,mode='region')
DIM(2)
SB(c=0.5)
ZF()
FT(negateImag=True)
PHASE(ph0=0.0,ph1=0.0)
DIM()
DPHASE(dim=2)
DPHASE(dim=1,firstOrder=True)
run()