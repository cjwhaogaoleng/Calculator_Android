package com.example.counter.util.advancedCalculator; /**
 * BigDecimalMath���еķ������˽׳��������з������������趨�ľ��Ⱥ���ܼ��㣬
 * ���Բ�ʹ�þ�̬����������Ҫ�ȳ�ʼ��һ���������þ��ȣ�Ȼ���ٵ��÷���
 * 
 * ���е���ʾ��ϢӦ���ı�������ʾ�����Ż�..................................................................................................................................................................
 */

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalMath{
    private final int accuracy; //���ȣ����㵽С�����λ�����ǵ������������Ҫ���趨���ȸ�2λ
    private final BigDecimal accuracyNum; //=10^-accuracy�����ݾ������õ���ֵ������������С�ڸ�ֵ˵���ﵽ�趨���ȣ��˳�����

    private final BigDecimal _105_095; //���ڶ�������Ļ�׼ֵ��1.05/0.95�ľ�ȷ��ֵ
    private final BigDecimal log_105_095; //���ڶ�������Ļ�׼ֵ��ln(1.05/0.95)�ľ�ȷ��ֵ���ں�����ln(1.1)
    private BigDecimal log10; //���ڶ�������Ļ�׼ֵ��log10��Ҫ���ݾ���ʵʱ����ģ�ֱ�Ӽ����ٶȽ�������ֳɽ�С�����ۼ�
    private final BigDecimal atan05; //���ڷ����Ǻ�������Ļ�׼ֵ��arctan(0.5)�ľ�ȷ��ֵ
    private final BigDecimal PI2; //PI/2��ֵ

    //Բ����PI��ֵ��Ӧ�����þ��Ⱥ�ʵʱ���㣬�˴���ʱʹ�øù̶�ֵ����ȷ��С�����100λ
    private final static BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");

    //��Ȼ����e��ֵ��Ӧ�����þ��Ⱥ�ʵʱ���㣬�˴���ʱʹ�øù̶�ֵ����ȷ��С�����100λ
    private final static BigDecimal E = new BigDecimal("2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274");

    public BigDecimalMath(int ac){ //���캯������ʼ���������ʹ�õĻ�׼ֵ
        accuracy = ac + 2; //����ʱ�ľ���Ҫ�����þ��ȸ�2λ

        accuracyNum = BigDecimal.ONE.divide(BigDecimal.TEN.pow(accuracy)); // =1/10^-accuracy
        _105_095 = new BigDecimal("1.05").divide(new BigDecimal("0.95"),accuracy,BigDecimal.ROUND_HALF_EVEN); //����1.05/0.95�ľ�ȷ��ֵ
        log_105_095 = log_095_105(_105_095); //����ln(1.05/0.95)�ľ�ȷ��ֵ��log_095_105()�����Ĳ����ڽӽ�1ʱ�����Ͽ죬Զ��1������Ҫʹ�øú���
        atan05 = atan(new BigDecimal("0.5")); //���ڷ����Ǻ�������Ļ�׼ֵ����������һ������ȷ�ĺ���������Ϊprivate���ͣ�ֻ���ڳ�ʼ�������Ż�........................................................
        PI2 = PI.divide(new BigDecimal("2"),accuracy,BigDecimal.ROUND_HALF_EVEN); //PI/2��������

        //�ҵ����ʵ�һ�����������ӽ�1����˽������10����Ҫ��������С�������������׼ֵln10�����Ż�.............................................................................................................
        log10 = new BigDecimal("0");
        for(int i=0;i<10;i++){
            log10 = log10.add(log_095_105(new BigDecimal("1.25")));
        }
        log10 = log10.add(log_095_105(new BigDecimal("1.073741824")));

        //��ʼ��PI�ľ�ȷ��ֵ..................................................................................................................................................................................
        //��ʼ��E�ľ�ȷ��ֵ...................................................................................................................................................................................
    }

    // ���㷴���к��������������ò���
    // public BigDecimal acot(BigDecimal x){
    //     return PI2.subtract(atan(x)).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    // }

    // ���㷴���к�����������ΧΪʵ����
    public BigDecimal atan(BigDecimal x){
        //̩��չ��ʽ��x�ڽӽ���>1ʱ�����ر���
        //�����к�������ȡֵ��ΧΪʵ����
        //����arctan(-x) = -arctan(x),��������Χ���Ƶ���ʵ��
        //���x>1,����arctan(x) = PI/2-arctan(1/x),��arctan(1/x)����������Χת����[0, 1]����
        //��x�ڽӽ�1ʱ(��0.999)������Ȼ���������������������Ƶ�[0, 0.5]����
        //���x>0.5������arctan(x)=arctan(y)+arctan((x-y)/(1+xy))���˴�ȡy=0.5����arctan�Ĳ������Ƶ�0.5���£��˴�����ѡ��С������ֵ�ﵽ������ٶȣ����Ż�......................................................................

        boolean isMinus = false; //�����Ƿ�Ϊ����־λ
        boolean isGt1 = false; //�����Ƿ����1��־λ
        boolean isGt05 = false; //�����Ƿ����0.5��־λ

        if(x.signum() == -1){ //����Ϊ����������arctan(x)=-arctan(-x)����������ȡ��
            x = x.negate();
            isMinus = true;
        }

        if(x.compareTo(BigDecimal.ONE) > 0){ //��������1��ȡ�䵹��
            x = BigDecimal.ONE.divide(x,accuracy,BigDecimal.ROUND_HALF_EVEN);
            isGt1 = true;
        }

        if(x.compareTo(new BigDecimal("0.5")) > 0){ //��������0.5����������ת��
            BigDecimal fm = x.multiply(new BigDecimal("0.5")).add(BigDecimal.ONE);
            x = x.subtract(new BigDecimal("0.5")).divide(fm,accuracy,BigDecimal.ROUND_HALF_EVEN);
            isGt05 = true;
        }

        BigDecimal res = new BigDecimal("0"); //������
        BigDecimal term = new BigDecimal("0"); //��������ÿһ��
        int i = 0; //��������

        do{ //������̸���arctan(x)��̩��չ��ʽ��
            term = x.pow(4*i+1).divide(new BigDecimal(4*i+1),accuracy,BigDecimal.ROUND_HALF_EVEN);
            term = term.subtract(x.pow(4*i+3).divide(new BigDecimal(4*i+3),accuracy,BigDecimal.ROUND_HALF_EVEN));
            res = res.add(term);
            i++;
        }while(term.compareTo(accuracyNum) > 0); //�ۼӵ���С���趨������ֵʱ�˳�

        if(isGt05){ //�����������0.5���������arctan(0.5)
            res = atan05.add(res);
        }
        if(isGt1){ //�����������1��ʹ��PI/2��ȥ���
            res = PI2.subtract(res);
        }
        if(isMinus){ //�������Ϊ���������ȡ��
            res = res.negate();
        }

        return res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //���������Ϊ�趨���ȷ���
    }

    //���㷴���Һ�����������ΧΪ[-1,1]
    public BigDecimal acos(BigDecimal x){
        //��������Ϊ[-1, 1]
        //arcsin(x)+arccos(x)=PI/2;

        return PI2.subtract(asin(x)).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //����PI2-arcsin(x)
    }

    //���㷴���Һ�����������ΧΪ[-1,1]
    public BigDecimal asin(BigDecimal x){
        //�����Һ���ʹ�÷����к�������
        //arcsin(x) = arctan(x/sqrt(1-x^2))

        boolean isMinus = false;

        if(x.signum() == -1){ //����Ϊ��������arcsin(x)=-arcsin(-x)���Բ����ͽ��ȡ��
            x = x.negate();
            isMinus = true;
        }

        if(x.compareTo(BigDecimal.ONE) > 0){ //��������Ϊ[-1, 1]
            System.out.println("��ֵ������,������Ϊ[-1,1]");
            return BigDecimal.ZERO;
        }
        else if(x.compareTo(BigDecimal.ONE) == 0){ //arcsin(1)��ֱ�ӷ���PI/2��1Ϊ�����޷�����
            return PI2;
        }

        //�������湫ʽ����arcsin(x)
        BigDecimal res = pow(x,new BigDecimal("2"));
        res = BigDecimal.ONE.subtract(res);
        res = pow(res,new BigDecimal("0.5"));
        res = x.divide(res,accuracy,BigDecimal.ROUND_HALF_EVEN);
        res = atan(res);

        return (isMinus ? res.negate() : res).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //�������к�������λΪ����
    public BigDecimal tan(BigDecimal x){
        //tan(x) = sin(x)/cos(x)
        //���������10^accuracy˵���������,�Ӹ��ж�����.....................................................................................................................................................................

        if(x.abs().compareTo(PI2) >= 0){ //tan(x)�Ķ�����Ϊ(-PI/2,PI/2)
            System.out.println("��ֵ������,������Ϊ(-PI/2,PI/2)");
            return BigDecimal.ZERO;
        }

        return sin(x).divide(cos(x),accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //�������Һ�������λΪ����
    public BigDecimal cos(BigDecimal x){
        //�Ƚ�����ת����[0,2PI]����
        //�ٽ�����ת����[0,PI/2]����
        //�������������ʹ��[0,PI/2]�����ֵ��Ч����

        boolean isMinus = false;

        x = x.abs(); //ȫȡ��ֵ����Ϊcos(x) = cos(-x)
        int quotient = x.divideToIntegralValue(PI2).intValue(); //�����������PI/2���̣���ȥ����
        x = x.remainder(PI); //ȡx/(PI/2)������

        switch(quotient % 4){ //�жϲ�����Ч��[0,PI/2],[PI/2,PI],[PI,3PI/2],[3PI/2,2PI]�ĸ�����
            case 1:x = PI.subtract(x);isMinus = true;break; //[PI/2,PI]����
            case 2:isMinus = true;break; //[PI,3PI/2]����
            case 3:x = PI.subtract(x);break; //[3PI/2,2PI]����
        }

        BigDecimal res = new BigDecimal("0");
        BigDecimal term = new BigDecimal("0");
        int i = 0;

        do{ //����cos(x)��̩��չ��ʽ��
            term = x.pow(2*i).divide(fac(2*i),accuracy,BigDecimal.ROUND_HALF_EVEN);
            res = res.add(i%2==1 ? term.negate() : term);
            i++;
        }while(term.compareTo(accuracyNum) > 0);

        return (isMinus ? res.negate() : res).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //�������Һ�������λΪ����
    public BigDecimal sin(BigDecimal x){
        //������̲ο�cos(x)��ֻ��̩��չ��ʽ��һ��

        boolean isMinus = false;

        if(x.compareTo(BigDecimal.ZERO) < 0){
            x = x.negate();
            isMinus = !isMinus;
        }

        int quotient = x.divideToIntegralValue(PI2).intValue();
        x = x.remainder(PI);

        switch(quotient % 4){
            case 1:x = PI.subtract(x);break;
            case 2:isMinus = !isMinus;break;
            case 3:x = PI.subtract(x);isMinus = !isMinus;break;
        }

        BigDecimal res = new BigDecimal("0");
        BigDecimal term = new BigDecimal("0");
        int i = 0;

        do{
            term = x.pow(2*i+1).divide(fac(2*i+1),accuracy,BigDecimal.ROUND_HALF_EVEN);
            res = res.add(i%2==1 ? term.negate() : term);
            i++;
        }while(term.compareTo(accuracyNum) > 0);

        return (isMinus ? res.negate() : res).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //�Ƕ�ת����
    public BigDecimal toRadians(BigDecimal deg){
        deg = deg.divide(new BigDecimal("180"),accuracy,BigDecimal.ROUND_HALF_EVEN);
        return deg.multiply(PI).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //����ת�Ƕ�
    public BigDecimal toDegrees(BigDecimal rad){
        rad = rad.multiply(new BigDecimal("180"));
        return rad.divide(PI,accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //����a��x�η�
    public BigDecimal pow(BigDecimal a,BigDecimal x){
        //���ȼ������Ϸ��ԣ����Ż�..............................................................................................................................................................................
        //0^0������
        //a^0=1,a!=0
        //��aΪ����ʱ��x����Ϊż��
        //��aΪ�Ǹ���ʱ��x��Ϊ������
        //.........................

        boolean isMinus = false;

        if(x.signum() == -1){ //xΪ�������Ƚ�xȡ��Ȼ��Խ��ȡ����
            x = x.negate();
            isMinus = true;
        }

        try{ //�ж�x�Ƿ�Ϊ������Ϊ�����Ļ�ֱ��ʹ��BigDecimal��pow����
            x.intValueExact();
        }catch(Exception e){ //x��Ϊ������ʹ��a^x��̩��չ��ʽ���㣬���¼�����̲ο�a^x��̩��չ��ʽ
            BigDecimal xlna = x.multiply(log(a)).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //����x*ln(a)
            BigDecimal res = new BigDecimal("0");
            BigDecimal term = new BigDecimal("0");
            int i = 0;
    
            do{
                term = xlna.pow(i).divide(fac(i),accuracy,BigDecimal.ROUND_HALF_EVEN);
                res = res.add(term);
                i++;
            }while(term.abs().compareTo(accuracyNum) > 0); //�˴������þ���ֵ�Ƚϣ���Ϊxlna����Ϊ����
    
            return isMinus ? BigDecimal.ONE.divide(res,accuracy,BigDecimal.ROUND_HALF_EVEN) : res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
        }

        BigDecimal res = a.pow(x.intValue()).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //xΪ������ֱ�ӵ���BigDecimal��pow����
        return isMinus ? BigDecimal.ONE.divide(res,accuracy,BigDecimal.ROUND_HALF_EVEN) : res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //��aΪ��x�Ķ�������������
    public BigDecimal log(BigDecimal a,BigDecimal x){
        //��aΪ��x�Ķ���=log(x)/log(a)
        BigDecimal res = log(x).divide(log(a),accuracy,BigDecimal.ROUND_HALF_EVEN);
        return res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //������eΪ��x�Ķ���
    public BigDecimal log(BigDecimal x){
        //�ú�������log_095_105����ʵ�ֶ�(0,+����)��Χ�Ĳ��������
        //����x�������0
        //�����ŵ�(0.5, 5]���õ�ln(10)
        //�����ŵ�(0.95, 1.05]���õ�ln(1.1........)

        if(x.compareTo(BigDecimal.ZERO) <= 0){
            System.out.println("�����������0");
            return BigDecimal.ZERO;
        }

        BigDecimal res = new BigDecimal("0");
        int ln10Count = 0; //10�������ۼӱ���
        int ln1_1Count = 0; //1.1�������ۼӱ���

        //ln(x)=a*ln(10)+b*ln(1.1)+ln(y)
        //����x=10a*1.1b*y
        while(x.compareTo(new BigDecimal("5")) > 0){ //��������5��10����С����ѭ��ִ��xlog10�Σ���(��10Ϊ��x�Ķ���)��
            x = x.divide(BigDecimal.TEN,accuracy,BigDecimal.ROUND_HALF_EVEN); //���ò�������10
            ln10Count++; //�ۼ�һ�δ��������һ��10����С
        }
        while(x.compareTo(new BigDecimal("0.5")) < 0){ //����С��0.5,10���Ŵ󣬸�ѭ��ִ��xlog0.1����(��0.1Ϊ��x�Ķ���)��
            x = x.multiply(BigDecimal.TEN); //����������10
            ln10Count--; //�ۼ�һ�δ��������һ��10���Ŵ�
        }

        while(x.compareTo(new BigDecimal("1.05")) > 0){ //��������1.05��1.1����С����ѭ�����ִ��24�Σ���10/(1.1^24)<1.05
            x = x.divide(_105_095,accuracy,BigDecimal.ROUND_HALF_EVEN);
            ln1_1Count++; //�ۼ�һ�δ��������һ��1.1����С
        }
        while(x.compareTo(new BigDecimal("0.95")) < 0){ //����С��0.95��1.1���Ŵ󣬸�ѭ�����ִ��7�Σ���0.5*(1.1^7)>0.95
            x = x.multiply(_105_095);
            ln1_1Count--; //�ۼ�һ�δ��������һ��1.1���Ŵ�
        }
        x = x.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //������������λ

        res = log10.multiply(new BigDecimal(ln10Count)); //����ln10Count*ln(10)��ֵ
        res = res.add(log_105_095.multiply(new BigDecimal(ln1_1Count))); //����ln1_1Count*ln(1.1)��ֵ

        return res.add(log_095_105(x)).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //�������ŵ�[0.95, 1.05]�����ln(x)��ֵ
    }

    //��Ҫ���ڼ���[0.95,1.05]�����ڵ�ln(x)��ֵ���������������Ҳ���ԣ���ԽԶ�롰1������Խ�������Բ��Լ���ln(10)��ź�ʱ3s
    private BigDecimal log_095_105(BigDecimal x){
        if(x.compareTo(BigDecimal.ZERO) <= 0){ //ln(x)�в���x�������0
            System.out.println("�����������0");
            return BigDecimal.ZERO;
        }

        //ln(x)��̩��չ����ln(x)=2y(y^0/1 + y^2/3 + y^4/5 + y^6/7 + ...)
        //���У�y=(x-1)/(x+1)
        BigDecimal y = (x.subtract(BigDecimal.ONE)).divide(x.add(BigDecimal.ONE),accuracy,BigDecimal.ROUND_HALF_EVEN); //�ȼ����y
        BigDecimal res = new BigDecimal("0"); //�ۼӵĽ��
        BigDecimal term = new BigDecimal("0"); //��ѭ���м���ÿһ��ۼ�
        int i = 0; // �ۼӱ���

        do{
            term = y.pow(2*i).divide(new BigDecimal(2*i+1),accuracy,BigDecimal.ROUND_HALF_EVEN); //����չ����ͨ�ʽ�����i��
            res = res.add(term); //ÿ���ۼ�
            i++; //��i��
        }while(term.compareTo(accuracyNum) > 0); //���ۼӵ���С�ھ���Ҫ��ʱ�˳��������Ѵﵽ�趨����

        res = res.multiply(y);
        res = res.multiply(new BigDecimal("2")); //����ǰ���2y
        
        return res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //�����������Ϊaccuracy
    }

    //����׳ˣ�Ӧ����ֵΪ�Ǹ�������BigDecimal������С�����Զ�ȥβ�����븺�����1
    public BigDecimal fac(int n){
        if(n < 0){//����û�н׳�
            System.out.println("����û�н׳�");
            return BigDecimal.ZERO;
        }

        BigInteger res = new BigInteger("1");
        BigInteger Bn = BigInteger.valueOf(n); //��nתΪBigInteger����

        while(Bn.compareTo(BigInteger.ONE) > 0){ //����1��ʱ���۳�
            res = res.multiply(Bn);
            Bn = Bn.subtract(BigInteger.ONE); //�Լ�1
        }

        return new BigDecimal(res); //�׳˽��һ��Ϊ����������Ҫ�涨����
    }

    public int getAccuracy(){ //�����趨����
        return accuracy;
    }

    public BigDecimal getPI(){ //����Բ����PI��ֵ�����ܽ�PI����Ϊ���������ΪPI��ֵ�����趨�ľ��ȶ��ı�
        return PI.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getE(){ //������Ȼ����E��ֵ�����ܽ�E����Ϊ���������ΪE��ֵ�����趨�ľ��ȶ��ı�
        return E.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }
}
