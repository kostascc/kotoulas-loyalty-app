package com.google.zxing.multi;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

public final class ByQuadrantReader implements Reader {
    private final Reader delegate;

    public ByQuadrantReader(Reader reader) {
        this.delegate = reader;
    }

    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, ChecksumException, FormatException {
        return decode(binaryBitmap, null);
    }

    public com.google.zxing.Result decode(com.google.zxing.BinaryBitmap r6, java.util.Map<com.google.zxing.DecodeHintType, ?> r7) throws com.google.zxing.NotFoundException, com.google.zxing.ChecksumException, com.google.zxing.FormatException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/156856360.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = r6.getWidth();
        r1 = r6.getHeight();
        r0 = r0 / 2;
        r1 = r1 / 2;
        r2 = 0;
        r3 = r5.delegate;	 Catch:{ NotFoundException -> 0x0018 }
        r4 = r6.crop(r2, r2, r0, r1);	 Catch:{ NotFoundException -> 0x0018 }
        r6 = r3.decode(r4, r7);	 Catch:{ NotFoundException -> 0x0018 }
        return r6;
    L_0x0018:
        r3 = r5.delegate;	 Catch:{ NotFoundException -> 0x002a }
        r4 = r6.crop(r0, r2, r0, r1);	 Catch:{ NotFoundException -> 0x002a }
        r3 = r3.decode(r4, r7);	 Catch:{ NotFoundException -> 0x002a }
        r4 = r3.getResultPoints();	 Catch:{ NotFoundException -> 0x002a }
        makeAbsolute(r4, r0, r2);	 Catch:{ NotFoundException -> 0x002a }
        return r3;
    L_0x002a:
        r3 = r5.delegate;	 Catch:{ NotFoundException -> 0x003c }
        r4 = r6.crop(r2, r1, r0, r1);	 Catch:{ NotFoundException -> 0x003c }
        r3 = r3.decode(r4, r7);	 Catch:{ NotFoundException -> 0x003c }
        r4 = r3.getResultPoints();	 Catch:{ NotFoundException -> 0x003c }
        makeAbsolute(r4, r2, r1);	 Catch:{ NotFoundException -> 0x003c }
        return r3;
    L_0x003c:
        r2 = r5.delegate;	 Catch:{ NotFoundException -> 0x004e }
        r3 = r6.crop(r0, r1, r0, r1);	 Catch:{ NotFoundException -> 0x004e }
        r2 = r2.decode(r3, r7);	 Catch:{ NotFoundException -> 0x004e }
        r3 = r2.getResultPoints();	 Catch:{ NotFoundException -> 0x004e }
        makeAbsolute(r3, r0, r1);	 Catch:{ NotFoundException -> 0x004e }
        return r2;
    L_0x004e:
        r2 = r0 / 2;
        r3 = r1 / 2;
        r6 = r6.crop(r2, r3, r0, r1);
        r0 = r5.delegate;
        r6 = r0.decode(r6, r7);
        r7 = r6.getResultPoints();
        makeAbsolute(r7, r2, r3);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.multi.ByQuadrantReader.decode(com.google.zxing.BinaryBitmap, java.util.Map):com.google.zxing.Result");
    }

    public void reset() {
        this.delegate.reset();
    }

    private static void makeAbsolute(ResultPoint[] resultPointArr, int i, int i2) {
        if (resultPointArr != null) {
            for (int i3 = 0; i3 < resultPointArr.length; i3++) {
                ResultPoint resultPoint = resultPointArr[i3];
                resultPointArr[i3] = new ResultPoint(resultPoint.getX() + ((float) i), resultPoint.getY() + ((float) i2));
            }
        }
    }
}
