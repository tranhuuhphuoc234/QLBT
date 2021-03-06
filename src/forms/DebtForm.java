package forms;

import utils.ChangePosition;
import utils.DBConnection;
import utils.setUIFont;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebtForm extends JDialog {
    int row;
    public static Dimension d;
    public static TableDebt td;
    public DebtForm(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 300);
        setModal(true);
        setResizable(false);
        setUIFont f = new setUIFont();
        f.Font(new FontUIResource("Arial", Font.PLAIN, 12));
        setTitle("Debt");
        //add root panel
        JPanel rootPnl = (JPanel) getContentPane();
        rootPnl.setLayout(new BoxLayout(rootPnl, BoxLayout.Y_AXIS));
        //create box
        Box[] boxes = new Box[2];
        boxes[0] = Box.createHorizontalBox();
        boxes[1] = Box.createHorizontalBox();

        boxes[0].createGlue();
        boxes[1].createGlue();

        rootPnl.add(boxes[0]);
        rootPnl.add(boxes[1]);

        //add JPanel head
        JPanel pnlHead = new JPanel();
        pnlHead.setLayout(null);

        //add component to panel head

        JTextField tfSearch = new JTextField();
        tfSearch.setBounds(10, 40, 200, 25);
        pnlHead.add(tfSearch);

        String[] boxColumn = {"All","Index","Supplier","Value","Payment Date","Status"};
        JComboBox boxSearch = new JComboBox(boxColumn);
        boxSearch.setBounds(230, 40, 120, 25);
        pnlHead.add(boxSearch);

        JButton btnChk = new JButton("Check payment date");
        btnChk.setBounds(380,40,180,25);
        pnlHead.add(btnChk);

        btnChk.addActionListener(e -> {
            String nearPaymentDate = "";
            long currentDay = System.currentTimeMillis();
            long dayInMil = 604800000 ;
            DBConnection db = new DBConnection();
            for ( int i = 0 ;i<db.getRowCount("debt");i++)
            {
                String expDayInString  = ((JLabel)td.pnlData.get(i).getComponent(3)).getText();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse(expDayInString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                long result = date.getTime()-currentDay;

                if(  result <= dayInMil)
                {
                    String index = ((JLabel)td.pnlData.get(i).getComponent(0)).getText();
                    String Date = ((JLabel)td.pnlData.get(i).getComponent(3)).getText();
                    if(!((JLabel)td.pnlData.get(i).getComponent(4)).getText().equals("Đã thanh toán"))
                    nearPaymentDate = nearPaymentDate +"Index: "+index +" - Payment Period: "+Date+"\n";
                }

            }

            ExpForm epx = new ExpForm(nearPaymentDate,"Payment Period");
        });


//        JButton btnSearch = new JButton("Search");
//        btnSearch.setBounds(120, 80, 120, 25);
        //add JPanel body
        JPanel pnlBody = new JPanel();
        pnlBody.setLayout(new GridLayout(1, 0));
        //add component to panel body
        String[] columnname = {"","Supplier","Value","Payment Period","Status"};
        String query = "select * from debt";
        d = new Dimension(140, 20);
        td = new TableDebt();
        JScrollPane sp = td.table("debt", columnname, query, d, true);
        //tao su kien search
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                DBConnection db = new DBConnection();
                ChangePosition c = new ChangePosition();
                int position = 0;
                int index = boxSearch.getSelectedIndex();// lay vi tri index trong boxSearch
                String strSearch = tfSearch.getText(); // lay text trong text field
                row = db.getRowCount("debt"); // lay so hang
                int column = 0;
                if (index == 0 ) // tuong duong voi All trong boxSearch
                {
                    for (JPanel item : td.pnlData
                    ) {
                        item.show();
                    }
                } else {
                    for (int i = 0; i < row; i++) {
                        if (td.getLabels().get(index-1 + column).getText().matches(".*" + strSearch + ".*"))
                        {
                            td.pnlData.get(i).show();
                            for (int secondIndex = 0; secondIndex < td.pnlAllData.getComponentCount(); secondIndex++) {
                                if (td.pnlAllData.getComponent(secondIndex) == (td.pnlData.get(i))) {
                                    c.swap(td.pnlAllData, position, secondIndex);
                                    position++;
                                }
                            }
                        } else {
                            td.pnlData.get(i).hide();
                        }
                        column += td.column;
                    }
                    //redraw lai panel
                    td.pnlAllData.revalidate();
                    td.pnlAllData.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                DBConnection db = new DBConnection();
                ChangePosition c = new ChangePosition();
                int position = 0;
                int index = boxSearch.getSelectedIndex();// lay vi tri index trong boxSearch
                String strSearch = tfSearch.getText(); // lay text trong text field
                row = db.getRowCount("inventory"); // lay so hang
                int column = 0;
                if (index == 0 ) // tuong duong voi All trong boxSearch
                {
                    for (JPanel item : td.pnlData
                    ) {
                        item.show();
                    }
                } else {
                    for (int i = 0; i < row; i++) {
                        if (td.getLabels().get(index-1 + column).getText().matches(".*" + strSearch + ".*"))
                        {
                            td.pnlData.get(i).show();
                            for (int secondIndex = 0; secondIndex < td.pnlAllData.getComponentCount(); secondIndex++) {
                                if (td.pnlAllData.getComponent(secondIndex) == (td.pnlData.get(i))) {
                                    c.swap(td.pnlAllData, position, secondIndex);
                                    position++;
                                }
                            }
                        } else {
                            td.pnlData.get(i).hide();
                        }
                        column += td.column;
                    }
                    //redraw lai panel
                    td.pnlAllData.revalidate();
                    td.pnlAllData.repaint();
                }
            }
        });
        pnlBody.add(sp);

        //add 2 panel to box
        pnlBody.setPreferredSize(new Dimension(1300, 400));
        pnlHead.setPreferredSize(new Dimension(1300, 200));
        boxes[0].add(pnlHead);
        boxes[1].add(pnlBody);

        setVisible(true);
    }
}
