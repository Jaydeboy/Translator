package translator;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.darkprograms.speech.translator.GoogleTranslate;
public class Translator {

    public static void main(String[] args) {

        StringBuilder holder = new StringBuilder();

        try {
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;

            is = new FileInputStream("c:/data/test.txt");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String line;

            while ((line = br.readLine()) != null) {
                //Translate to English
                System.out.println(line);
                String translated = GoogleTranslate.translate("en", line);

                System.out.println(translated);
                holder.append(translated).append("\n");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = holder.toString();
        System.out.println("\n-------" + content);
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("c:/data/translated.txt");
            bw = new BufferedWriter(fw);
            bw.write(content);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null) {
                    bw.close();
                }

                if (fw != null) {
                    fw.close();
                }

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

    }

}
