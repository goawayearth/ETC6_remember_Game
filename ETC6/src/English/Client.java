package English;
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;


public class Client extends JFrame implements Runnable,ActionListener{
    boolean flag=true;
    JPanel jpl=new JPanel();
    JTextField jtf=new JTextField();//用户输入答案,需要监听
    JLabel result=new JLabel();//存放反馈用语
    JLabel grade=new JLabel();//存放得分

    JLabel[] JLabels=new JLabel[4];//选项数组
    JLabel jLabel=new JLabel();//存放掉落单词
    InputStream is = null;
    BufferedReader br =null;
    PrintStream ps = null;//客户端输出流

    int score=10;//记录分数
    Thread th=new Thread(this);//使用多线程，目的是使用延迟函数sleep

    String right;//存放正确结果的选项号
    String word1;//临时存放找到的单词
    String translation0,translation1,translation2,translation3;//临时存放找到的汉语

    File f1=new File("D:\\STUDY\\JAVA\\ETC6\\未掌握单词.txt");
    File f2=new File("D:\\STUDY\\JAVA\\ETC6\\已掌握单词.txt");

    FileWriter writer1=new FileWriter(f1,true);//通过设置true来进行追加文件
    FileWriter writer2=new FileWriter(f2,true);
    JDialog jDialog=new JDialog(this,"Fighting",true);//开始对话框
    JTextField nicheng=new JTextField();
    JLabel ready=new JLabel("你准备好了吗？");
    JLabel enterNi=new JLabel("昵称：");
    JButton yes=new JButton("YES");

    JDialog jieshu=new JDialog(this,"OVER",true);//结束时候的对话框

    void setTheTitle(String str)//给页面设置标题
    {
        this.setTitle(str);
    }
    public Client ()throws Exception  //构造函数
    {
        JPanel j1=new JPanel();//开始时候的面板
        j1.setBackground(Color.YELLOW);
        j1.add(ready);
        j1.add(nicheng);
        j1.add(enterNi);
        j1.add(yes);
        j1.setLayout(null);
        jDialog.add(j1);//面板加在对话框上面，设置颜色
        jDialog.setLocation(800,200);
        enterNi.setLocation(10,10);
        nicheng.setLocation(100,10);
        ready.setLocation(10,60);
        yes.setLocation(100,130);

        yes.setSize(80,50);
        jDialog.setSize(400,600);
        enterNi.setSize(100,30);
        nicheng.setSize(80,30);
        ready.setSize(200,30);



        ready.setFont(new Font("宋体",Font.BOLD,20));
        enterNi.setFont(new Font("宋体",Font.BOLD,20));


        yes.addActionListener(
                new AbstractAction() {    //绑定
                    public void actionPerformed(ActionEvent e) {  //处理函数
                        jDialog.setVisible(false);//点击按钮后，对话框不可见
                        setTheTitle(nicheng.getText());
                    }
                });
        jDialog.setVisible(true);//开始时，对话框可见

        JLabels[0]=new JLabel();//实例化每一个选项标签
        JLabels[1]=new JLabel();
        JLabels[2]=new JLabel();
        JLabels[3]=new JLabel();
        jpl.setLayout(null);
        this.add(jpl);        //加面板
        jpl.add(jLabel);
        jpl.add(JLabels[0]);
        jpl.add(JLabels[1]);
        jpl.add(JLabels[2]);
        jpl.add(JLabels[3]);
        jpl.add(jtf);
        jpl.add(result);
        jpl.add(grade);



        JLabels[0].setSize(200,30);//设置大小
        JLabels[1].setSize(200,30);
        JLabels[2].setSize(200,30);
        JLabels[3].setSize(200,30);
        jLabel.setSize(400,40);
        jtf.setSize(200,30);
        result.setSize(300,40);
        grade.setSize(100,30);

        JLabels[0].setLocation(20,250);//设置位置
        JLabels[1].setLocation(20,300);
        JLabels[2].setLocation(20,350);
        JLabels[3].setLocation(20,400);
        jtf.setLocation(20,450);
        result.setLocation(40,100);
        grade.setLocation(300,20);

        jLabel.setFont(new Font("宋体",Font.BOLD,20));   //设置字体
        JLabels[0].setFont(new Font("宋体",Font.BOLD,15));
        JLabels[1].setFont(new Font("宋体",Font.BOLD,15));
        JLabels[2].setFont(new Font("宋体",Font.BOLD,15));
        JLabels[3].setFont(new Font("宋体",Font.BOLD,15));
        result.setFont(new Font("宋体",Font.BOLD,15));
        grade.setFont(new Font("宋体",Font.BOLD,15));

        JLabels[0].setOpaque(true);//设置颜色
        JLabels[1].setOpaque(true);
        JLabels[2].setOpaque(true);
        JLabels[3].setOpaque(true);
        JLabels[0].setBackground(new Color(233, 250, 111, 255));
        JLabels[1].setBackground(new Color(233, 250, 111, 255));
        JLabels[2].setBackground(new Color(233, 250, 111, 255));
        JLabels[3].setBackground(new Color(233, 250, 111, 255));
        jpl.setBackground(new Color(250, 186, 186, 255));

        jtf.addActionListener(this);  //实现绑定


        this.setSize(400,600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(800,200);
        this.setVisible(true);

        Music music=new Music();
        grade.setText("得分："+score);  //开局之前，输出初始化分数
        connect();      //将connect放入构造函数，使之自动调用
        Thread t = new Thread(music);////////////////////////////////////////////////////////////////////////多加了一个线程，双线程工作
        t.start();
        th.start();
    }

    public void connect()throws Exception//与服务器进行连接
    {
        Socket s = new Socket("127.0.0.1", 1111);//客户端连接服务器，并双方通信
        is = s.getInputStream();
        br = new BufferedReader(new InputStreamReader(is));
        ps = new PrintStream(s.getOutputStream());
    }
    public void choice()throws Exception//给每一个选项赋值
    {
        word1=br.readLine();//读取从服务器传进来的单词
        String[] tr=new String[4];
        translation0=br.readLine();//读取从服务器传进来的翻译
        translation2=br.readLine();
        translation1=br.readLine();
        translation3=br.readLine();
        tr[0]=translation0;//0处存放正确答案
        tr[1]=translation1;
        tr[2]=translation2;
        tr[3]=translation3;


        int[] position=new int[4];         //一个数组，对应四个选项，打乱排序
        position[0]=(int)(Math.random()*4);
        do{
            position[1]=(int)(Math.random()*4);
        }while(position[1]==position[0]);
        do{
            position[2]=(int)(Math.random()*4);
        }while(position[2]==position[0]||position[2]==position[1]);
        do{
            position[3]=(int)(Math.random()*4);
        }while(position[3]==position[0]||position[3]==position[1]||position[3]==position[2]);


        JLabels[0].setText("A:"+tr[position[0]]);//四个翻译顺序打乱，放入ABCD四个按钮
        JLabels[1].setText("B:"+tr[position[1]]);
        JLabels[2].setText("C:"+tr[position[2]]);
        JLabels[3].setText("D:"+tr[position[3]]);
        if(position[0]==0)right="A";
        if(position[1]==0)right="B";
        if(position[2]==0)right="C";
        if(position[3]==0)right="D";
        jLabel.setText(word1);//将找到的单词放在可以掉落的标签上
        jLabel.setLocation(115,0);

    }

    public void run()//重写run函数，实现从顶部掉落
    {
        while (true) {
            try
            { jtf.setText("");//在每一轮开始之前，将输入框置空
                choice();//在每一轮开始时，重新从文档里找单词
            }
            catch(Exception e){}

            while(true)
            {
                try {Thread.sleep(200);} //延时掉落
                catch (Exception e) {}
                jLabel.setLocation(jLabel.getX(), jLabel.getY()+10);

                if(jLabel.getY()>=50)
                    result.setText("");//因为新一轮开始了，所以要将反馈框清空

                if (jLabel.getY() > jpl.getHeight())//如果单词都超界了，说明未回答
                {
                    result.setText("您没有回答,正确答案是"+right);
                    try
                    {
                        if(flag)
                        {
                            writer1.write(word1);//将此单词及其翻译写进“未掌握文档”
                            writer1.write("\n");
                            writer1.write(translation0 + "(未作答)");
                            writer1.write("\n");
                            writer1.flush();//文档刷新
                        }
                    }
                    catch (Exception ex){}
                    score--;//分数减一
                    grade.setText("得分："+score);//输出分数
                    if(score<=0)
                    {
                       // overplay();
                        Over();
                        break;

                    }
                    ps.println(""+score);//传给服务器
                    break;
                }

                if(jLabel.getX()==113) { //如果X=113，说明已经执行了performed函数，需要跳出本循环，进行下一轮答题
                    if(score<=0)
                    {
                        //overplay();
                        Over();
                        break;

                    }
                    ps.println("" + score);//传给服务器
                    break;
                }

            }
        }
    }


    public void	actionPerformed(ActionEvent e)//编写处理函数
    {
        String option=jtf.getText();//用option来存用户输入的答案
        if(option.equals(right))
        {

            result.setText("恭喜回答正确！");

            try{
                if(flag) {
                    writer2.write(word1);//将此单词及其翻译写进“已掌握文档”
                    writer2.write("\n");
                    writer2.write(translation0);
                    writer2.write("\n");
                    writer2.flush();
                }
            }catch(Exception e1){}

            score++;//答对加一分
            grade.setText("得分："+score);//输出得分
            jLabel.setLocation(113,jLabel.getY()-jLabel.getY());
            //回答之后，让单词回到顶部，通过X的值来判断是否运行了performed函数

        }
        else
        {
            result.setText("回答错误，答案是"+right);
            try {
                if(flag) {
                    writer1.write(word1);//将此单词及其翻译写进“已掌握文档”
                    writer1.write("\n");
                    writer1.write(translation0 + "(答错)");
                    writer1.write("\n");
                    writer1.flush();
                }
            }catch (Exception e2){}
            score--;
            score--;//答错减两分
            grade.setText("得分："+score);//输出得分
            jLabel.setLocation(113,jLabel.getY()-jLabel.getY());
            //回答之后，让单词回到顶部，通过X的值来判断是否运行了performed函数
        }
    }

    public void overplay()
    {
        for(int i=0;i<4;i++)
        {
            jpl.remove(JLabels[i]);
        }
        jpl.remove(jLabel);
        jpl.remove(grade);
        jpl.remove(result);
        jpl.remove(jtf);
        jpl.repaint();
    }

    void Over()//结束时的操作
    {

        JButton overButton=new JButton("关闭");

        JButton review=new JButton("复习");
        grade.setText("得分：0");
        JLabel isOver=new JLabel();
        isOver.setFont(new Font("宋体",Font.BOLD,20));
        overButton.setFont(new Font("宋体",Font.BOLD,20));
        review.setFont(new Font("宋体",Font.BOLD,20));
        //overplay();
        JPanel j2=new JPanel();
        j2.setBackground(Color.YELLOW);
        j2.add(isOver);//结束时的对话框
        j2.add(overButton);//关闭按钮
        j2.add(review);//复习按钮
        jieshu.add(j2);//////////将组件加在面板上，将面板放在Dialog上（因为Dialog本身没有形状，且Dialog和Jframe同时存在，优先处理Dialog）

        j2.setLayout(null);

        isOver.setText("游戏结束!");
        isOver.setSize(200,30);
        overButton.setSize(80,60);
        review.setSize(80,60);
        jieshu.setSize(300,220);

        isOver.setLocation(70,30);
        overButton.setLocation(50,90);
        review.setLocation(150,90);
        jieshu.setLocation(850,300);

        overButton.addActionListener(// //绑定  事件处理
                new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {  //处理函数
                        ps.println("exit");//关闭服务器
                        System.exit(0);//结束程序
                    }
                });
        review.addActionListener(// //绑定  事件处理
                new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {  //处理函数
                        ps.println("review");//关闭服务器
                        score=10;
                        flag=false;
                        grade.setText("得分："+score);//输出得分
                      //  jieshu.setVisible(false);
                        jieshu.dispose();
                    }
                });
        jieshu.setVisible(true);
    }


    public static void main(String[] args)throws Exception {
        new Client();
    }
}
