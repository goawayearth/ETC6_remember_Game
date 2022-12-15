package English;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Vector;
import javax.swing.*;
public class Sever extends JFrame {

    ServerSocket ss;
    Socket s;
    JPanel jpl=new JPanel();
    File f=new File("D:\\STUDY\\JAVA\\ETC6\\word1.txt");//传入文件的位置
    File f1=new File("D:\\STUDY\\JAVA\\ETC6\\未掌握单词.txt");
    File f2=new File("D:\\STUDY\\JAVA\\ETC6\\已掌握单词.txt");
    FileReader fr=new FileReader(f);//因为BufferedReader的构造函数的参数需要用到FileReader类，故将f传入
    BufferedReader br=new BufferedReader(fr);//用BufferedReader里面的ReadLine函数进行读
    FileReader fr1=new FileReader(f1);//因为BufferedReader的构造函数的参数需要用到FileReader类，故将f传入
    BufferedReader bur1=new BufferedReader(fr1);//用BufferedReader里面的ReadLine函数进行读
    FileReader fr2=new FileReader(f2);//因为BufferedReader的构造函数的参数需要用到FileReader类，故将f传入
    BufferedReader bur2=new BufferedReader(fr2);//用BufferedReader里面的ReadLine函数进行读

    Vector<String> list = new Vector<String>();//建立一个容器，将读取的内容放进去
    Vector<String> past=new Vector<String>();//建立一个容器，将已掌握和未掌握两个文档传入
    String word,translation;//临时存放读取的内容
    PrintStream ps = null;//服务器输出流
    String score;//记录分数
    InputStream is = null;
    BufferedReader br1 =null;
    String word1;//临时存放找到的单词
    String translation0,translation1,translation2,translation3;//临时存放找到的汉语
    /////////////////////////////////////////////////////////////////////////////////////////加了三个JLable
    JLabel sum=new JLabel("单词总数：2089");
    JLabel residue=new JLabel();
    JLabel already=new JLabel();
    JLabel review=new JLabel();
    int reviewNum=0;
   boolean flagRE=true;//flag=1的时候是学习新的单词，flag=0的时候，就是复习学过的单词****************************************
    //修改找单词的函数，和显示的函数，和转变的过程
    boolean IfNew()//判断此单词是否已经背诵过
    {
        boolean flag=true;
        for(int i=0;i<past.size();i++)
        {
            if(word1.equals(past.get(i))&&flagRE)//新学的时候判断，不新学的时候不需要
            {
                flag=false;
                break;//如果找到了相等的，则跳出此循环，说明该单词已经背诵过，需要重新choose单词
            }
        }

        return flag;
    }
    void init()//初始化三个jlable
    {
        jpl.setLayout(null);  //对this的操作
        jpl.add(sum);
        jpl.add(residue);
        jpl.add(already);
        jpl.add(review);

        sum.setSize(300,30);
        sum.setLocation(20,50);
        sum.setFont(new Font("宋体",Font.BOLD,30));

        residue.setSize(300,30);
        residue.setLocation(20,150);
        residue.setFont(new Font("宋体",Font.BOLD,30));
        if(flagRE)residue.setText("未学习单词："+(2089-past.size()));

        already.setSize(300,30);
        already.setLocation(20,100);
        already.setFont(new Font("宋体",Font.BOLD,30));
        if(flagRE)already.setText("已学习单词："+past.size());

        review.setSize(300,30);
        review.setLocation(20,200);
        review.setFont(new Font("宋体",Font.BOLD,30));
        review.setText("本次已复习单词："+reviewNum);
    }
    public Sever ()throws Exception  //构造函数
    {
        Readtext();//读取已经传输过的单词
        Read();//将Read函数放进构造函数，使之能自动调用
        init();

        this.add(jpl);//加面板
        this.setTitle("服务器");
        this.setVisible(true);
        this.setSize(400,600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(300,200);
        jpl.setBackground(new Color(248, 171, 110, 255));


        connect();//将connect函数放进构造函数，使之能自动调,进行连接
        do {       //将找到的单词和翻译传到客户端
            choose();
            ps.println(word1);
            ps.println(translation0);
            ps.println(translation1);
            ps.println(translation2);
            ps.println(translation3);
            if(flagRE)past.add(word1);//传输过的单词加入past
            init();
            score=br1.readLine(); //每一轮结束后判断分数是否为零
            if(score.equals("exit"))
            {
                System.exit(1);
            }
            if(score.equals("review"))
            {
                flagRE=false;
                past.clear();//past清空，把未学会的读进来
                FileReader fr1=new FileReader(f1);//因为BufferedReader的构造函数的参数需要用到FileReader类，故将f传入
                BufferedReader bur1=new BufferedReader(fr1);//用BufferedReader里面的ReadLine函数进行读
                while(true)
                {
                    String ifo=null;
                    try{ ifo = bur1.readLine();}catch(Exception e){}
                    if (ifo == null) break;
                    System.out.println(ifo);
                    past.add(ifo);  //把已经读取过未掌握的单词放入past容器内
                }
                try{ bur1.close();}catch (Exception e) {}
            }
        }while(!(score.equals("0")||score.equals("-1")));

    }

    public void Readtext()////读取已经传输过的单词
    {while(true)
    {
        String ifo=null;
        try{ ifo = bur1.readLine();}catch(Exception e){}
        if (ifo == null) break;
        past.add(ifo);  //把已经读取过未掌握的单词放入past容器内
    }
        try{ bur1.close();}catch (Exception e) {}
        while(true)
        {
            String ifo=null;
            try{ ifo = bur2.readLine();}catch(Exception e){}
            if (ifo == null) break;
            past.add(ifo);  //把已经读取过已掌握的单词放入past容器内
        }
        try{ bur2.close();}catch (Exception e) {}

    }

    public void Read()throws  Exception //读取文件函数
    {
        while(true)
        {

            word=br.readLine();  //读取一行单词 即一个英语单词
            if(word==null) break;//如果单词位置为空，说明已经读完，直接跳出
            translation=br.readLine();  //读取一行中文 即一个对应的汉语
            list.add(word);//单词放进双数位置
            list.add(translation);//翻译放进单数位置
        }
    }

    public void connect()throws Exception //定义服务器和客户端进行连接的函数
    {
        ss=new ServerSocket(1111);//启动服务器，接受客户端连接；如果服务器连上，则往下运行accept;未连上，则等待连接
        s=ss.accept();
        System.out.println("连接成功");
        ps = new PrintStream(s.getOutputStream());//服务器向客户端传信息
        is = s.getInputStream();//客户端向服务器传信息
        br1 = new BufferedReader(new InputStreamReader(is));//服务器读信息
    }
    public void choose()throws Exception //找到每局所用的一个单词和四个汉语
    {  int i=0;
        if(flagRE) {
            do {
                i = (int) (2089 * Math.random()) * 2;//找一个单词的位置，在偶数位置
                word1 = list.get(i);
            } while (!IfNew());
        }
        if(!flagRE)
        {
            do {
                i = ((int) ((past.size())/2 * Math.random())) * 2;//在past里找一个单词的位置，在偶数位置
                word1 = list.get(i);
            } while (!IfNew());
            reviewNum++;
        }
        int [] b=new int[4];//声明四个选项的位置
        b[0]=i+1;//找到的单词对应的汉语的位置，先将正确答案放进一个选项里面
        if(flagRE) {
            boolean flag = false;
            //其余三个选项 随机找三个汉语的位置
            do {
                b[1] = ((int) (2088 * Math.random())) * 2 + 1;//奇数位置上是汉语
                if (b[1] != b[0]) flag = true;
            } while (!flag);  //只要相等就一直生成随机数，直到不相等时，跳出
            flag = false;
            do {
                b[2] = ((int) (2088 * Math.random())) * 2 + 1;//奇数位置上是汉语
                if (b[2] != b[0] && b[2] != b[1]) flag = true;
            } while (!flag);//只要相等就一直生成随机数，直到不相等时，跳出
            flag = false;
            do {
                b[3] = ((int) (2088 * Math.random())) * 2 + 1;//奇数位置上是汉语
                if (b[3] != b[0] && b[3] != b[1] && b[3] != b[2]) flag = true;
            } while (!flag);//只要相等就一直生成随机数，直到不相等时，跳出
        }
        if(!flagRE)
        {
            boolean flag = false;
            //其余三个选项 随机找三个汉语的位置
            do {
                b[1] = ((int) ((past.size()-1)/2 * Math.random())) * 2 + 1;//奇数位置上是汉语
                if (b[1] != b[0]) flag = true;
            } while (!flag);  //只要相等就一直生成随机数，直到不相等时，跳出
            flag = false;
            do {
                b[2] = ((int) ((past.size()-1)/2 * Math.random())) * 2 + 1;//奇数位置上是汉语
                if (b[2] != b[0] && b[2] != b[1]) flag = true;
            } while (!flag);//只要相等就一直生成随机数，直到不相等时，跳出
            flag = false;
            do {
                b[3] = ((int) ((past.size()-1)/2 * Math.random())) * 2 + 1;//奇数位置上是汉语
                if (b[3] != b[0] && b[3] != b[1] && b[3] != b[2]) flag = true;
            } while (!flag);//只要相等就一直生成随机数，直到不相等时，跳出
        }

        if(flagRE)
        {
            word1=list.get(i);//得到位置上对应的单词
            translation0=list.get(b[0]);//得到位置上对应的汉语
            translation1=list.get(b[1]);
            translation2=list.get(b[2]);
            translation3=list.get(b[3]);
        }
        else
        {
            System.out.println(i+" "+b[0] + " "+b[1]+" "+b[2] + " "+b[3]);
            word1=past.get(i);//得到位置上对应的单词
            translation0=past.get(b[0]);//得到位置上对应的汉语
            translation1=past.get(b[1]);
            translation2=past.get(b[2]);
            translation3=past.get(b[3]);
           // past.remove(word1);
           // past.remove(translation0);
            for(int j=0;j<past.size(); j++) {
                System.out.println(past.get(j));
            }

        }

    }

    public static void main(String[] args) throws Exception{
        Sever a=new Sever();

    }

}


