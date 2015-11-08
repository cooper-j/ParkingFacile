package parkingfacile.hexan.com.Controller;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import parkingfacile.hexan.com.Model.Park;

/**
 * Created by james_000 on 9/20/2015.
 */
public abstract class AbstractCity {

    protected ReentrantLock mLock = new ReentrantLock();

    protected OnParksFoundListener onParksFoundListener;
    protected OnHttpErrorListener onHttpErrorListener;

    public AbstractCity(){
    }

    abstract public void getParks();

    abstract public void getParksStatus();

    abstract public void getParksDetail();

    abstract public void getParksFree();

    abstract public void getParksTimeAndPrice();

    public ArrayList<Park> getParkList(){
        return null;
    }

    public interface OnParksFoundListener {
        public enum TypeVille{
            RENNES,
            NANTES,
            PARIS,
            BORDEAUX,
            STRASBOURG
        }

        void onParksFound(ArrayList<Park> parkArrayList, TypeVille ville);
    }

    public interface OnHttpErrorListener {
        void onHttpError(String msg);
    }

    public void setParksFoundListener(OnParksFoundListener listener) {
        onParksFoundListener = listener;
    }

    public void setHttpErrorListener(OnHttpErrorListener listener) {
        onHttpErrorListener = listener;
    }
}
