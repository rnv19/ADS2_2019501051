import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import java.lang.Math;
import java.util.Arrays;
/**
 * SeamCarver
 */
public class SeamCarver {
    private Picture picture;
    // private double[][] energyMatrix;
    private double[][] cumMatrix;
    // private double[][] tranEnergyMatrix;
    private boolean temporary = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if(picture == null){
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
        // newPicture = new Picture(picture.width() - 1, picture.height());
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private int getRed(int rgb) {
        int r;
        r = (rgb >> 16) & 0xFF;
       return r;
    }

    private int getGreen(int rgb) {
        int b;
        b = (rgb >> 8) & 0xFF;
        return b;
    }

    private int getBlue(int rgb) {
        int g;
        g = (rgb >> 0) & 0xFF;
        return g;
    }



    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        int rgb;
        int rgbLeft;
        int rgbRight;
        int rgbUp;
        int rgbDown;
        if ((x < 0 || x >= picture.width()) || (y < 0 || y >= picture.height())) { throw new IllegalArgumentException(); }
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1 ) return 1000;
        rgb = picture.getRGB(x,y);
        rgbLeft = picture.getRGB(x,y+1);
        rgbRight = picture.getRGB(x,y-1);
        rgbUp = picture.getRGB(x-1,y);
        rgbDown = picture.getRGB(x+1,y);

        int temp1 = getRed(rgbRight) - getRed(rgbLeft);
        int temp2 = getGreen(rgbRight) - getGreen(rgbLeft);
        int temp3 = getBlue(rgbRight) - getBlue(rgbLeft);
        int temp4 = getRed(rgbUp) - getRed(rgbDown);
        int temp5 = getGreen(rgbUp) - getGreen(rgbDown);
        int temp6 = getBlue(rgbUp) - getBlue(rgbDown);

        double deltax = Math.pow(temp1, 2) + Math.pow(temp2, 2) + Math.pow(temp3, 2);
        double deltay = Math.pow(temp4, 2) + Math.pow(temp5, 2) + Math.pow(temp6, 2);
        return Math.sqrt((deltax + deltay));
    }

    private double[][] createEnergyMatrix(int hg, int wd) {
        double[][] energyMatrix = new double[hg][wd];
        for (int i = 0; i < hg; i++) {
            for (int j = 0; j < wd; j++) {
                energyMatrix[i][j] = energy(j, i);
            }
        }
        // for (int j = 0; j < energyMatrix.length; j++) {
        //     System.out.println(Arrays.toString(energyMatrix[j]));
        // }
        // createCumMatrix(energyMatrix);
        return energyMatrix;
    }

    private double[][] createCumMatrix(double[][] energyMatrix) {
        // System.out.println(height() + " " + width());
        // System.out.println(energyMatrix.length + " " + energyMatrix[0].length);
        cumMatrix = new double[energyMatrix.length][energyMatrix[0].length];
        
        // System.out.println("Length = " + energyMatrix.length + " width =" + energyMatrix[0].length);
        
        for (int i = 0; i < energyMatrix[0].length; i++) {
            cumMatrix[0][i] = 1000;
        }
        for (int x = 1; x < energyMatrix.length; x++) {
            
            for (int y = 0; y < energyMatrix[0].length; y++) {
                // System.out.println(x + " " + y);
                if (y == 0) {
                    if (cumMatrix[x-1][y] < cumMatrix[x-1][y+1]) {
                        cumMatrix[x][y] = energyMatrix[x][y] + cumMatrix[x-1][y];
                    } else {
                        cumMatrix[x][y] = energyMatrix[x][y] + cumMatrix[x-1][y+1];
                    }
                } else if (y == energyMatrix[0].length-1) {
                    if (cumMatrix[x-1][y-1] < cumMatrix[x-1][y]) {
                        cumMatrix[x][y] = energyMatrix[x][y] + cumMatrix[x-1][y-1];
                    } else {
                        cumMatrix[x][y] = energyMatrix[x][y] + cumMatrix[x-1][y];
                    }
                } else {
                    // System.out.println("x = " + x + "y = " + y);
                    if ((cumMatrix[x-1][y-1] < cumMatrix[x-1][y]) && (cumMatrix[x-1][y-1] < cumMatrix[x-1][y+1])) {
                        cumMatrix[x][y] = energyMatrix[x][y] + cumMatrix[x-1][y-1];
                    } else if ((cumMatrix[x-1][y] < cumMatrix[x-1][y-1]) && (cumMatrix[x-1][y] < cumMatrix[x-1][y+1])) {
                        cumMatrix[x][y] = energyMatrix[x][y] + cumMatrix[x-1][y];
                    } else {
                        cumMatrix[x][y] = energyMatrix[x][y] + cumMatrix[x-1][y+1];
                    }
                }
                
            }
        }

        // System.out.println();
        // for (int x = 0; x < energyMatrix.length; x++) {
        //     System.out.println(Arrays.toString(cumMatrix[x]));
        // }
        return cumMatrix;

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if(width() == 1 || height() == 1){
            int[] sample = new int[height()];
            return sample;
        }
        // double[][] energyMatrix;
        if (!temporary) {
            // System.out.println("in");
            cumMatrix = createCumMatrix(createEnergyMatrix(picture.height(), picture.width()));
        }
        int[] temp = new int[cumMatrix.length];
        // double[][] arr = createCumMatrix(energyMatrix);
        double min = Double.MAX_VALUE;
        
        int temp1 = 0;
        int size = cumMatrix.length-1;
        int last = cumMatrix.length-1;
        for (int y = 0; y < cumMatrix[0].length; y++) {
            // System.out.println(last + " " + y);
            if (cumMatrix[last][y] < min) {
                min = cumMatrix[last][y];
                temp1 = y;
            }
        }
        int y = temp1;
        // System.out.println(y);
        temp[size--] = y;
        for (int x = cumMatrix.length - 1; x > 0; x--) {
            if (y == 0) {
                if (cumMatrix[x-1][y] < cumMatrix[x-1][y+1]) {
                    temp[size--] = y;
                }else {
                    temp[size--] = y+1;
                    y = y+1;
                }
            }else if (y == cumMatrix[0].length - 1) {
                if (cumMatrix[x-1][y-1] < cumMatrix[x-1][y]) {
                    temp[size--] = y-1;
                    y = y-1;
                }else {
                    temp[size--] = y;
                }
            }else {
                if (cumMatrix[x-1][y-1] < cumMatrix[x-1][y] && cumMatrix[x-1][y-1] < cumMatrix[x-1][y+1]) {
                    temp[size--] = y-1;
                    y = y-1;
                }else if (cumMatrix[x-1][y] < cumMatrix[x-1][y-1] && cumMatrix[x-1][y] < cumMatrix[x-1][y+1]) {
                    // System.out.println(cumMatrix[x-1][y-1] + " " + cumMatrix[x-1][y] + " " + cumMatrix[x-1][y+1]);
                        temp[size--] = y;
                } else {
                    temp[size--] = y+1;
                    y = y+1;
                }
            }
        }
        // System.out.println(Arrays.toString(temp));
        if (temp[1] == 0) {
            temp[0] = 0;
        } else temp[0] = temp[1]-1;

        // cumMatrix = createCumMatrix(energyMatrix);
        // System.out.println();
        // System.out.println(Arrays.toString(temp));
        return temp;
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] energyMatrix;
        double[][] tranEnergyMatrix;
        if(width() == 1 || height() == 1){
            int[] temp = new int[width()];
            return temp;
        }
        energyMatrix = createEnergyMatrix(picture.height(), picture.width());
        // System.out.println(picture.height() + " " + picture.width());
        // System.out.println(height() + " " + width());
        tranEnergyMatrix = new double[width()][height()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                tranEnergyMatrix[j][i] = energyMatrix[i][j];
            }
        }
        // double[][] temp7 = new double[width()][height()];
        // temp7 = createCumMatrix(tranEnergyMatrix);

        // System.out.println();
        // for (int k = 0; k < tranEnergyMatrix.length; k++) {
        //     System.out.println(Arrays.toString(tranEnergyMatrix[k]));
        // }
        cumMatrix = createCumMatrix(tranEnergyMatrix);
        // energyMatrix = tranEnergyMatrix;
        temporary = true;
        int[] sample = findVerticalSeam();
        // int[] tempHorizontal = findVerticalSeam();
        // System.out.println("Horizontal positions: " + Arrays.toString(horizontalPosition));
        // energyMatrix = createCumMatrix(energyMatrix);
        temporary = false;
        return sample;

    }



    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        Picture newPicture;

        if (seam  == null || seam.length != width()) { throw new IllegalArgumentException(); }
        for (int k = 0; k < seam.length - 1; k++) {
            if (seam[k] < 0 || seam[k] >= height()) { throw new IllegalArgumentException(); }
            int t = Math.abs(seam[k] - seam[k+1]);
            // System.out.println(t);
            if (t > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (seam[seam.length - 1] < 0 || seam[seam.length - 1] >= height()) { throw new IllegalArgumentException(); }

        // if(width() == 1 || height() == 1){
        //     return
        // }
        newPicture = new Picture(picture.width(), picture.height() - 1);
        Boolean temp;
        // System.out.println(picture);
        // System.out.println(newPicture);
        for (int i = 0; i < newPicture.width(); i++) {
            temp = false;
            for (int j = 0; j < newPicture.height(); j++) {
                // System.out.println(j + " " + i);
                if (j == seam[i]) temp = true;
                if (temp == true) {
                    newPicture.set(i,j,picture.get(i, j+1));
                } else {
                    newPicture.set(i, j, picture.get(i, j));
                }
            }
        }
        // System.out.println(Arrays.toString(seam));
        // System.out.println(newPicture);
        picture = newPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        Picture newPicture;
        if (seam  == null || seam.length != height()) { throw new IllegalArgumentException(); }
        for (int k = 0; k < seam.length - 1; k++) {
            if (seam[k] < 0 || seam[k] >= width()) { throw new IllegalArgumentException(); }
            int t = Math.abs(seam[k] - seam[k+1]);
            // System.out.println(t);
            if (t > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (seam[seam.length - 1] < 0 || seam[seam.length - 1] >= width()) { throw new IllegalArgumentException(); }
        newPicture = new Picture(picture.width() - 1, picture.height());
        Boolean temp;
        // System.out.println(Arrays.toString(seam));
        // newPicture.show();
        // System.out.println(picture);
        // System.out.println(newPicture);
        for (int i = 0; i < newPicture.height(); i++) {
            // System.out.println(newPicture.width());
            temp = false;
            for (int j = 0; j < newPicture.width(); j++) {
                // System.out.println(j + " " + i);
                if (j == seam[i]) temp = true;
                if (temp == true) {
                    newPicture.set(j, i, picture.get(j+1, i));
                } else {
                    newPicture.set(j, i, picture.get(j, i));
                }
            }
        }
        // int newPictureWidth = newPicture.width();
        // System.out.println(newPicture);
        // System.out.println(newPictureWidth);
        picture = newPicture;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // In in = new In(args[0]);
        // Picture pic = new Picture("D:\\MSIT\\ADS - 2\\ADS2_2019501051\\Week 2\\Client programs\\10x12.png");
        Picture pic = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(pic);
        // double[][] temp =  sc.createEnergyMatrix(sc.height(), sc.width());
        // sc.createCumMatrix(temp);
        // System.out.println(Arrays.toString(sc.findVerticalSeam()));
        // System.out.println();
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        // System.out.println(sc.picture());
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        // System.out.println(sc.picture());
        // System.out.println("ceck: " + Arrays.toString(sc.findVerticalSeam()));
        sc.removeVerticalSeam(sc.findVerticalSeam());
        // System.out.println(sc.picture());
        sc.removeVerticalSeam(sc.findVerticalSeam());
        // System.out.println(Arrays.toString(sc.findHorizontalSeam()));
        // int[] seam = sc.findVerticalSeam();
        // int[] seam = new int[]{ 1, 0, 0, 1, 0, 0, -1};
        // int[] seam = sc.findHorizontalSeam();
        // System.out.println(Arrays.toString(seam));
        // sc.removeVerticalSeam(seam);
        // sc.removeHorizontalSeam(seam);
        // for (int row = 0; row < sc.height(); row++) {
        //     for (int col = 0; col < sc.width(); col++)
        //         System.out.println(row + "," + col + "," + sc.energy(col, row));
        //     System.out.println();
        // }
    }
}