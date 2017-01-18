package brytskyi.waitershelperclient.app.activities.reports;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IReportService;
import org.joda.time.LocalDate;
import transferFiles.exceptions.DateException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.GenerateReportRequest;
import transferFiles.service.restService.restRequstObjects.GetByDateBeginEndRequest;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;


public class ReportsActivity extends AppCompatActivity {

    private IReportService service = (IReportService) MyApplication.getService();
    private User logined = MyApplication.getLoginedUser();
    private ProductsListInReportsFragment productsFragment;
    private DishesListInReportsFragment dishesFragment;
    private EditText dateBegin;
    private EditText dateEnd;
    private LocalDate beginDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();
    private Spinner typeReqSolt;
    private Button findButton;
    private Button printButton;
    private List<Denomination> denominations;
    private List<Ingridient> ingridients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        initVars();
        addListeners();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_in_reports);
        tabLayout.addTab(tabLayout.newTab().setText("Dishes"));
        tabLayout.addTab(tabLayout.newTab().setText("Products"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.reportsPager);
        ReportsPagerAdapter adapter = new ReportsPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        productsFragment = adapter.getProductsFragment();
        dishesFragment = adapter.getDishesFragment();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void addListeners() {
        dateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ReportsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        beginDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                        endDate = beginDate;
                        dateEnd.setText(endDate.toString());
                        dateBegin.setText(beginDate.toString());
                    }
                }, beginDate.getYear(), beginDate.getMonthOfYear() - 1, beginDate.getDayOfMonth()).show();
            }
        });

        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ReportsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                        dateEnd.setText(endDate.toString());
                    }
                }, endDate.getYear(), endDate.getMonthOfYear() - 1, endDate.getDayOfMonth()).show();
            }
        });

        typeReqSolt.setAdapter(new ArrayAdapter<String>(ReportsActivity.this,
                android.R.layout.simple_list_item_1, new String[]{"Require", "Solt"}));

        findButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (((String) typeReqSolt.getSelectedItem()).equals("Require")) {
                        if (beginDate.equals(endDate)) {
                            denominations = service.getRequireDenominationsForReport(beginDate);
                            if (denominations == null) {
                                showToast("No Values", ReportsActivity.this);
                                return;
                            }
                            dishesFragment.updateDishesList(denominations);
                            ingridients = service.countIngridientsForReport(denominations);
                            if (ingridients == null) ingridients = new LinkedList<Ingridient>();
                            productsFragment.updateProductsList(ingridients);
                        } else {
                            denominations = service.getRequireDenominationsForReport(new GetByDateBeginEndRequest(beginDate, endDate));
                            if (denominations == null) {
                                showToast("No Values", ReportsActivity.this);
                                return;
                            }
                            dishesFragment.updateDishesList(denominations);
                            ingridients = service.countIngridientsForReport(denominations);
                            if (ingridients == null) ingridients = new LinkedList<Ingridient>();
                            productsFragment.updateProductsList(ingridients);
                        }
                    } else {
                        if (beginDate.equals(endDate)) {
                            denominations = service.getSoltDenominationsForReport(beginDate);
                            if (denominations == null) {
                                showToast("No Values", ReportsActivity.this);
                                return;
                            }
                            dishesFragment.updateDishesList(denominations);
                            ingridients = service.countIngridientsForReport(denominations);
                            if (ingridients == null) ingridients = new LinkedList<Ingridient>();
                            productsFragment.updateProductsList(ingridients);
                        } else {
                            denominations = service.getSoltDenominationsForReport(new GetByDateBeginEndRequest(beginDate, endDate));
                            if (denominations == null) {
                                showToast("No Values", ReportsActivity.this);
                                return;
                            }
                            dishesFragment.updateDishesList(denominations);
                            ingridients = service.countIngridientsForReport(denominations);
                            if (ingridients == null) ingridients = new LinkedList<Ingridient>();
                            productsFragment.updateProductsList(ingridients);
                        }
                    }
                } catch (DateException e) {
                    showToast(e.getMessage(), ReportsActivity.this);
                }
            }
        });

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (denominations == null || denominations.isEmpty()) {
                    showToast("Nothing to generate in report!", ReportsActivity.this);
                    return;
                } else {
                    String date = beginDate.equals(endDate) ? beginDate.toString() : beginDate.toString() + "//" + endDate.toString();
                    try {
                        service.generateReport(new GenerateReportRequest(denominations, ingridients,
                                ((String) typeReqSolt.getSelectedItem()) + date));
                    } catch (FileNotFoundException e) {
                        showToast(e.getMessage(), ReportsActivity.this);
                    }
                }
            }
        });
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }


    private void initVars() {
        dateBegin = (EditText) findViewById(R.id.dateStartInReport);
        dateEnd = (EditText) findViewById(R.id.dateEndInReport);
        typeReqSolt = (Spinner) findViewById(R.id.spinnerInReport);
        findButton = (Button) findViewById(R.id.findButtonInReport);
        printButton = (Button) findViewById(R.id.printButtonInReport);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
