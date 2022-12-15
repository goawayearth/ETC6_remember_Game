package English;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class Music implements Runnable{
    public static void play(String path) {
        try {
            //相当于是将音乐文件放到播放器中
            Player player = new Player(new FileInputStream(new File(path)));
            //播放
            player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void run()
    {
        this.play("D:\\STUDY\\JAVA\\ETC6\\song1.mp3");
    }
}
