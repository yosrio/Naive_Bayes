import java.text.DecimalFormat;
import java.util.ArrayList;

public class Bayes {
    private Object[][] data;
    private Object[] category;
    private Object[][] input;
    private double[][] temp;
    public ArrayList<Key> key = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#.####");
    public ArrayList<Key> dataBaru = new ArrayList<>();

    public Bayes(Object[][] data, Object[] category, Object[][] input, int label, int jlhAtt){
        this.data = data;
        this.category = category;
        this.input = input;
        temp = new double[label][jlhAtt];
    }

    public void classify(int sum[]){
        int label[];
        int total = 0;
        for(int t = 0; t < sum.length; t++)
                total+=sum[t];
        
        for(int k = 0; k < input.length; k++){
            for(int y = 0; y < input[k].length; y++){
                label = new int[sum.length];                
                for(int i = 0; i < data.length; i++){
                    for(int j = 0; j < data[i].length; j++){
                        if(data[i][j].equals(input[k][y])){
                            for(int v = 0; v < key.size(); v++)
                                if(category[i].equals(key.get(v).getInfo()))
                                    label[key.get(v).getK()]++;                           
                        }
                    }
                }
                
                //P(Xk|Ci)
                for(int l = 0; l < label.length; l++){
                        temp[l][y] = (double)label[l]/sum[l];                       
                        System.out.println("P(Xk|Ci) variabel ke "+(y+1)+" = "
                                + "("+(double)label[l]+"/"+sum[l]+"="+df.format(temp[l][y])+")");
                }
                System.out.println("------------------------------------------");
            }

            // P(X|Ci)
            double X[] = new double[sum.length];
            for(int i = 0; i < temp.length; i++){
                for(int j = 0; j < temp[i].length; j++){
                    if(j!=0) // (47)
                        X[i] *= (double)temp[i][j];
                    else
                        X[i] = (double)temp[i][j]; 
                }
                System.out.println("P(X|Ci) : kelas ke ("+(i+1)+") = "+df.format(X[i]));
            }
            System.out.println("------------------------------------------");
            // P(X|Ci) * P(Ci)
            double c[] = new double[sum.length];
            for(int i = 0; i < X.length; i++){
                c[i] = (double)sum[i]/total;                
            }
            addNewLabel(c, X, k);
        }
    }

    public void addNewLabel(double c[], double X[], int k){
        double max = -1;
        String info = "";
        for(int i = 0; i < c.length; i++){
            if(c[i]*X[i] > max){
                max = c[i]*X[i];
                info = key.get(i).getInfo();
            }
            if(i==c.length-1)
                dataBaru.add(new Key(k, info, max));
            
            System.out.println("P(X|Ci) * P(Ci) kelas ke ("+(i+1)+") = "+df.format(c[i]*X[i]));
        }
    }

    public ArrayList<Key> getNewLabel(){
        return dataBaru;
    }
}