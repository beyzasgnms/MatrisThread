package matristhread;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Multiply extends MatrixThread {
    
    private int x, y, z;
    
    public Multiply(int x, int y) {
        this.x = x;
        this.y = y;
        // z değişkeni thread'imizin hangi satır üzerinde işlem yapacağını gösteren değişken
        this.z = 0;
    }
    
    public synchronized void multiplyMatrix() {
        int toplam = 0;
        // Dıştaki for döngümüz birinci matrisin satır sayısı kadar dönüyor
        for (int i = 0; i <= x; i++) {
            toplam = 0;
            // İçteki for döngümüz ikinci matrisin sütun sayısı kadar dönüyor
            for (int j = 0; j < y; j++) {
                // For döngüsü içinde matris çarpımı yapılıyor
                toplam = toplam +(m1[z][j] * m2[j][i]);
            }
            // Sonuç matrisinin uygun kısmına sonuç yazılıyor
            resultMatrix[z][i] = toplam;
        }
        if(z > x) return;
        // Threadler senkronize çalışacağı için her birinin üzerinde çalışacağı satırı bir sonraki satır olarak ayarlıyoruz
        z++;
    }
}

// Threadler bu sınıfın içerisinde devreye giriyor
// Runnable interface'ini implement eden bu sınıf, run metodu sayesinde threadleri devreye sokuyor
class MatrixMultiplier implements Runnable {
    
    private final Multiply mul;
    
    public MatrixMultiplier(Multiply mul) {
        this.mul = mul;
    }

    @Override
    public void run() {
        // Her bir thread bu metodu bir kez çalıştıracak ve bir satır üzerinde işlem yapacak
        mul.multiplyMatrix();
    }
}

// Ve main sınıfımız
public class MatrixThread {
    // Matrislerimizin büyüklüğüyle aynı büyüklükte iki boyutlu arrayler oluşturuluyor
    static int[][] m1 = new int[4][3];
    static int[][] m2 = new int[3][5];
    static int[][] resultMatrix = new int[4][5];

    public static void main(String[] args) {
        Scanner readM1 = null, readM2 = null;
        // Matris dosyaları okunuyor
        try {
            readM1 = new Scanner(new FileInputStream("Matris1.txt"));
            readM2 = new Scanner(new FileInputStream("Matris2.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadı");
            System.exit(0);
        }
        
        // Ve for döngüleri aracılığıyla arraylere aktarılıyor
        for(int i = 0; i < m1.length; i++) {
            String satir = readM1.nextLine();
            String[] degerler = satir.split(" ");
            for(int j = 0; j < degerler.length; j++) {
                m1[i][j] = Integer.parseInt(degerler[j]);
            }
        }
        for(int i = 0; i < m2.length; i++) {
            String satir = readM2.nextLine();
            String[] degerler = satir.split(" ");
            for(int j = 0; j < degerler.length; j++) {
                m2[i][j] = Integer.parseInt(degerler[j]);
            }
        }
        
        // Yazdırma metodu çağırıldı
        printMatris();
        
        // Çarpımı yapacak threadler burada oluşturuluyor
        try {
            // Matrislerimizi çarpan sınıftan bir nesne oluşturduk ve sonuç matrisinin büyüklüğünü parametre olarak verdik
            Multiply mul = new Multiply(4, 3);
            
            // Daha sonra threadlerin devreye gireceği run metodunu içeren sınıfımızdan ilk matrisimizin satır sayısı kadar nesne oluşturuyoruz
            // Her bir thread ilk matrisin bir satırını ve ikinci matrisin tüm sütunlarını dolaşıyor
            MatrixMultiplier thread1 = new MatrixMultiplier(mul);
            MatrixMultiplier thread2 = new MatrixMultiplier(mul);
            MatrixMultiplier thread3 = new MatrixMultiplier(mul);
            MatrixMultiplier thread4 = new MatrixMultiplier(mul);
            
            // Thread nesnesi oluşturulup ilgili parametreler veriliyor
            Thread th1 = new Thread(thread1);
            Thread th2 = new Thread(thread2);
            Thread th3 = new Thread(thread3);
            Thread th4 = new Thread(thread4);
            
            // Threadler çalıştırılıyor
            th1.start();
            th2.start();
            th3.start();
            th4.start();
            
            // Ve birbirlerini bölmesin diye bekletiliyor
            // Bu işlem yapılmadığı takdirde istenmeyen veya yanlış sonuçlar elde edilebilir
            th1.join();
            th2.join();
            th3.join();
            th4.join();
        } catch(InterruptedException e) {}
        
        // Sonuç matrisimiz yazdırılıyor
        printResult();
    }
    
    // Yazdırma metodları
    public static void printMatris() {
        System.out.println("Matris 1:");
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[i].length; j++) {
                System.out.print(m1[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Matris 2:");
        for (int i = 0; i < m2.length; i++) {
            for (int j = 0; j < m2[i].length; j++) {
                System.out.print(m2[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static void printResult() {
        System.out.println("");
        System.out.println("Sonuc Matrisi");
        System.out.println("-------------");
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int j = 0; j < resultMatrix[i].length; j++) {
                System.out.print(resultMatrix[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
