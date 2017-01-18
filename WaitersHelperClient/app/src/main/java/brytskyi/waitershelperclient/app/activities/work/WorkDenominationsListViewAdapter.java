package brytskyi.waitershelperclient.app.activities.work;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.denomination.Denomination;

import java.util.ArrayList;
import java.util.List;

public class WorkDenominationsListViewAdapter extends BaseAdapter {

    private List<DenomTimeWrapper> denominations;
    private Activity activity;

    public WorkDenominationsListViewAdapter(Activity activity, List<Denomination> denominations) {
        super();
        this.denominations = new ArrayList<DenomTimeWrapper>();
        for (Denomination denomination : denominations) {
            this.denominations.add(new DenomTimeWrapper(denomination, "0:00"));
        }
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return denominations.size();
    }

    @Override
    public Object getItem(int position) {
        return denominations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView dish;
        TextView price;
        TextView priceSum;
        TextView timeAdded;
        TextView timePassedLabel;
        TextView timePassed;
        TextView state;
        TextView portions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.denomination_list_row, null);
            holder = new ViewHolder();
            holder.dish = (TextView) convertView.findViewById(R.id.dishNameInDenomRowToEdit);
            holder.price = (TextView) convertView.findViewById(R.id.priceForPortionInDemonRowToEdit);
            holder.priceSum = (TextView) convertView.findViewById(R.id.priceSumInDenomRowToEdit);
            holder.timeAdded = (TextView) convertView.findViewById(R.id.timeDenomAddedInDenomRowToEdit);
            holder.timePassedLabel = (TextView) convertView.findViewById(R.id.timeDenomReadyInDenomRow);
            holder.timePassed = (TextView) convertView.findViewById(R.id.timeDenomReadyInDenomRowToEdit);
            holder.state = (TextView) convertView.findViewById(R.id.denomStateInDenomsRowToEdit);
            holder.portions = (TextView) convertView.findViewById(R.id.portionsInDenomRowToEdit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DenomTimeWrapper wrap = denominations.get(position);
        Denomination item = wrap.getDenomination();
        holder.dish.setText(item.getDish().getName());
        holder.price.setText(String.valueOf(item.getDish().getPriceForPortion()));
        holder.priceSum.setText(String.valueOf(item.getPrice()));
        holder.timeAdded.setText(item.getTimeWhenAdded().toString());
        holder.timePassedLabel.setText("Passed");
        holder.timePassed.setText(wrap.getTime());
        wrap.setView(holder.timePassed);
        holder.state.setText(item.getState().toString());
        holder.portions.setText(String.valueOf(item.getPortion()));
        return convertView;
    }

    private void runTimer(final DenomTimeWrapper wrapper) {
        Thread timerTh = new Thread(new Timer(wrapper));
        timerTh.start();
    }

    private class Timer implements Runnable {

        private long startTime = System.currentTimeMillis();
        private DenomTimeWrapper wrapper;

        public Timer(DenomTimeWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.currentThread().sleep(1000);
                    final String time = defineMinutesSeconds(System.currentTimeMillis());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (wrapper.getView() != null) wrapper.getView().setText(time);
                            wrapper.setTime(time);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private String defineMinutesSeconds(long l) {
            int seconds = (int) ((l - startTime) / 1000);
            int minutes = seconds / 60;
            int secondsOfMinute = seconds % 60;
            return minutes + ":" + secondsOfMinute;
        }
    }

    public void removeFromList(DenomTimeWrapper denomination) {
        denominations.remove(denomination);
        this.notifyDataSetChanged();
    }

    public void removeFromList(Denomination denomination) {
        DenomTimeWrapper den = null;
        for (DenomTimeWrapper wrapper : denominations) {
            if (wrapper.getDenomination().getId()==denomination.getId()) {
                den = wrapper;
                break;
            }
        }
        denominations.remove(den);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WorkDenominationsListViewAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public void addItem(Denomination denomination) {
        DenomTimeWrapper denom = new DenomTimeWrapper(denomination, "0:00");
        denominations.add(denom);
        runTimer(denom);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WorkDenominationsListViewAdapter.this.notifyDataSetChanged();
            }
        });

    }

}