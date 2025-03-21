import os
from pyproc import *
procOpts(nprocess=4)

FIDHOME, TMPHOME = getTestLocations()
FID(os.path.join(FIDHOME,'agilent/hnca3d.fid'))
CREATE(os.path.join(TMPHOME,'tst_ubiq_hnca.nv'))

acqOrder('21')
acqarray(0,0,0)
skip(0,0,0)
label('HN','CA','N')
acqsize(0,0,0)
tdsize(0,0,0)
sf('sfrq','dfrq','dfrq2')
sw('sw','sw1','sw2')
ref(7.47,'C','N')
DIM(1)
TDCOMB(dim=3,coef='echo-antiecho')
TDSS(shift='0.407f')
SB()
ZF()
FT()
PHASE(ph0=169.9,ph1=0.0,dimag=False)
EXTRACT(start=20,end=380,mode='region')
DIM(2)
SB(c=0.5)
ZF()
FT()
PHASE(ph0=-19.8,ph1=34.1,dimag=False)
DIM(3)
SB(c=0.5)
ZF()
FT()
PHASE(ph0=0.0,ph1=0.0)
DIM()
DPHASE(1)
DPHASE(2)
DPHASE(3)
DIM(1)
AUTOREGIONS()
BCPOLY()
DIM(2)
AUTOREGIONS(winSize=8,minBase=5,ratio=20.767)
BCWHIT(order=2)
DIM(3)
AUTOREGIONS(winSize=4,ratio=4)
BCWHIT()
run()
