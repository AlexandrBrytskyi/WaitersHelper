package brytskyi.waitershelperclient.app.tasksGetter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.activities.orderings.ConcretteOrderingActivity;
import brytskyi.waitershelperclient.app.activities.orderings.DenominationsListViewAdapter;
import brytskyi.waitershelperclient.app.activities.work.WorkActivity;
import brytskyi.waitershelperclient.app.activities.work.WorkDenominationsListViewAdapter;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.denomination.DenominationState;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;
import transferFiles.service.restService.ICookService;

import java.util.List;

public class TasksGetter implements Runnable {

    private ICookService service = (ICookService) MyApplication.getService();
    private User logined = MyApplication.getLoginedUser();

    private List<Denomination> newTasks;
    private List<Denomination> messages;
    int num = 0;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.currentThread().sleep(10000);
                newTasks = service.getNewDenominations(logined);
                makeNotifications(newTasks);
                messages = service.getMessages(logined);
                makeNotifications(messages);
            } catch (InterruptedException e) {
                Thread.currentThread().start();
            }
        }
    }

    private void makeNotifications(List<Denomination> newTasks) {
        if (newTasks != null && !newTasks.isEmpty()) {
            String message = "";
            for (Denomination newTask : newTasks) {
                num++;
                message += newTask.getDish().getName() + ", " + newTask.getState() + newTask.getPortion() + ", " + newTask.getOrder().getWhoServesOrder().getName() + "\n";
                makeNotification(newTask, message, num);
            }
        }
    }

    private void makeNotification(Denomination newTask, String message, int num) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MyApplication.getAppContext())
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("Work")
                        .setContentText(message)
                        .setContentIntent(definePendingIntent(newTask))
                        .setSound(alarmSound);

        NotificationManager mNotificationManager =
                (NotificationManager) MyApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(num, mBuilder.build());

    }

    private PendingIntent definePendingIntent(Denomination newTask) {
        Intent intent;
        if (logined.getType().equals(UserType.COLD_KITCHEN_COCK) ||
                logined.getType().equals(UserType.HOT_KITCHEN_COCK) ||
                logined.getType().equals(UserType.MANGAL_COCK)) {
            intent = new Intent(MyApplication.getAppContext(), WorkActivity.class);
            WorkDenominationsListViewAdapter adapter = (WorkDenominationsListViewAdapter) MyApplication.getFromContext("WorkDenomsAdapter");
            if (!isDenomCancelled(newTask)) {
                adapter.addItem(newTask);
            } else {
                adapter.removeFromList(newTask);
            }
        } else {
            if (logined.getType().equals(UserType.BARMEN)) {
                if (!newTask.getOrder().getWhoServesOrder().equals(logined)) {
                    intent = new Intent(MyApplication.getAppContext(), WorkActivity.class);
                    WorkDenominationsListViewAdapter adapter = (WorkDenominationsListViewAdapter) MyApplication.getFromContext("WorkDenomsAdapter");
                    if (!isDenomCancelled(newTask)) {
                        adapter.addItem(newTask);
                    } else {
                        adapter.removeFromList(newTask);
                    }
                } else {
                    intent = makeIntentUpdateDenom(newTask);
                }
            } else {
                intent = makeIntentUpdateDenom(newTask);
            }
        }
        return PendingIntent.getActivity(MyApplication.getAppContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent makeIntentUpdateDenom(Denomination newTask) {
        Intent intent;Ordering ordering = newTask.getOrder();
        intent = new Intent(MyApplication.getAppContext(), ConcretteOrderingActivity.class);
        intent.putExtra("ordering", ordering);
        DenominationsListViewAdapter adapter = (DenominationsListViewAdapter) MyApplication.getFromContext("denominationsAdapter");
        if (adapter != null) adapter.updateDenomination(newTask);
        return intent;
    }

    private boolean isDenomCancelled(Denomination denomination) {
        if (denomination.getState().equals(DenominationState.CANCELED_BY_ADMIN) ||
                denomination.getState().equals(DenominationState.CANCELED_BY_WAITER) ||
                denomination.getState().equals(DenominationState.CANCELED_BY_BARMEN)) return true;
        return false;
    }
}