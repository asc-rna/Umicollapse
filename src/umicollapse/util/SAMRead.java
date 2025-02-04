package umicollapse.util;

import htsjdk.samtools.SAMRecord;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SAMRead extends Read{
    private static Pattern defaultUMIPattern;
    private SAMRecord record;
    private int avgQual;
    private String umi ;
    public SAMRead(SAMRecord record){
        this.record = record;
        int idx = record.getReadName().lastIndexOf('_');
        this.umi = (idx == -1 || idx == record.getReadName().length() - 1) ? "" : record.getReadName().substring(idx + 1);

        float avg = 0.0f;

        for(byte b : record.getBaseQualities())
            avg += b;

        this.avgQual = (int)(avg / record.getReadLength());
    }

    public static void setDefaultUMIPattern(String sep){
        defaultUMIPattern = umiPattern(sep);
    }

    public static Pattern umiPattern(String sep){
        return Pattern.compile("^(.*)" + sep + "([ATCGN]+)(.*?)$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public BitSet getUMI(int maxLength){
        String umi_cut = this.umi;
        if (maxLength >= 0 && umi.length() > maxLength) {
            umi_cut = this.umi.substring(0, maxLength);
        }

        return Utils.toBitSet(umi_cut); // 假设 Utils.toBitSet() 内部已经做了 toUpperCase()        
    }

    @Override
    public int getUMILength(){
        // Matcher m = defaultUMIPattern.matcher(record.getReadName());
        // m.find();
        // return m.group(2).length();
        return umi.length();
    }

    @Override
    public int getAvgQual(){
        return avgQual;
    }

    @Override
    public boolean equals(Object o){
        SAMRead r = (SAMRead)o;
        return record.equals(r.record);
    }

    public int getMapQual(){
        return record.getMappingQuality();
    }

    public SAMRecord toSAMRecord(){
        return record;
    }
}
