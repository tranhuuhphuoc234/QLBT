package forms;

import utils.ChangePosition;
import utils.DBConnection;
import utils.setUIFont;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InventoryForm extends JDialog {
    int row;
    public static Dimension d;
    public static TableInventory tp;
    public InventoryForm(String storeId) {
        //setting form
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 500);
        setModal(true);
        setResizable(false);
        setUIFont f = new setUIFont();
        f.Font(new FontUIResource("Arial", Font.PLAIN, 12));
        setTitle("Inventory");
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

        String[] boxColumn = {"All","ImportID","Name", "Content", "Quantity", "Price", "Supplier","Import Date","Expiration Date"};
        JComboBox boxSearch = new JComboBox(boxColumn);
        boxSearch.setBounds(230, 40, 120, 25);
        pnlHead.add(boxSearch);

        JButton btnChkExp = new JButton("Check Expiration");
        btnChkExp.setFont(new Font("Arial",Font.PLAIN,10));
        btnChkExp.setBounds(10,90,120,25);


//        JButton btnSearch = new JButton("Search");
//        btnSearch.setBounds(120, 80, 120, 25);

        //add JPanel body
        JPanel pnlBody = new JPanel();
        pnlBody.setLayout(new GridLayout(1, 0));
        //add component to panel body
        String[] columnname = {"ImportID","Name", "Content", "Quantity", "Price", "Supplier","Import Date","Expiration Date"};
        String query = "select importid,productname , productcontent, inventory.quantity, inventory.price, suppliername,exportdate,expirationdate\n" +
                "from inventory join product on  inventory.productid = product.productid\n" +
                "join supplier on product.supplierid = supplier.supplierid where storeid ="+storeId;
        d = new Dimension(115, 20);
        tp = new TableInventory();
        JScrollPane sp = tp.table("inventory", columnname, query, d, true);
        //tao su kien search
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                DBConnection db = new DBConnection();
                ChangePosition c = new ChangePosition();
                int position = 0;
                int index = boxSearch.getSelectedIndex();// lay vi tri index trong boxSearch
                String strSearch = tfSearch.getText(); // lay text trong text field
                row = db.getRowCount("inventory"); // lay so hang
                int column = 0;
                if (index == 0 ) // tuong duong voi All trong boxSearch
                {
                    for (JPanel item : tp.pnlData
                    ) {
                        item.show();
                    }
                } else {
                    for (int i = 0; i < row; i++) {
                        if (tp.getLabels().get(index-1 + column).getText().matches(".*" + strSearch + ".*"))
                        {
                            tp.pnlData.get(i).show();
                            for (int secondIndex = 0; secondIndex < tp.pnlAllData.getComponentCount(); secondIndex++) {
                                if (tp.pnlAllData.getComponent(secondIndex) == (tp.pnlData.get(i))) {
                                    c.swap(tp.pnlAllData, position, secondIndex);
                                    position++;
                                }
                            }
                        } else {
                            tp.pnlData.get(i).hide();
                        }
                        column += tp.column;
                    }
                    //redraw lai panel
                    tp.pnlAllData.revalidate();
                    tp.pnlAllData.repaint();
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
                    for (JPanel item : tp.pnlData
                    ) {
                        item.show();
                    }
                } else {
                    for (int i = 0; i < row; i++) {
                        if (tp.getLabels().get(index-1 + column).getText().matches(".*" + strSearch + ".*"))
                        {
                            tp.pnlData.get(i).show();
                            for (int secondIndex = 0; secondIndex < tp.pnlAllData.getComponentCount(); secondIndex++) {
                                if (tp.pnlAllData.getComponent(secondIndex) == (tp.pnlData.get(i))) {
                                    c.swap(tp.pnlAllData, position, secondIndex);
                                    position++;
                                }
                            }
                        } else {
                            tp.pnlData.get(i).hide();
                        }
                        column += tp.column;
                    }
                    //redraw lai panel
                    tp.pnlAllData.revalidate();
                    tp.pnlAllData.repaint();
                }
            }
        });
        pnlHead.add(btnChkExp);
        btnChkExp.addActionListener(e -> {
            String outOfDateProduct = "";
            String nearOutOfDateProduct = "";
            long currentDay = System.currentTimeMillis();
            long dayInMil = 604800000 ;
            DBConnection db = new DBConnection();
            for ( int i = 0 ;i<db.getRowCount("inventory");i++)
            {
               String expDayInString  = ((JLabel)tp.pnlData.get(i).getComponent(7)).getText();
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               Date date = null;
                try {
                     date = sdf.parse(expDayInString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                if(  date.getTime() - currentDay <= dayInMil && date.getTime() -currentDay > 0)
                {
                    String imp = ((JLabel)tp.pnlData.get(i).getComponent(0)).getText();
                    String proName = ((JLabel)tp.pnlData.get(i).getComponent(1)).getText();
                    nearOutOfDateProduct = nearOutOfDateProduct + imp +" - "+proName+"\n";
                }
                else if ( date.getTime() -currentDay <= 0)
                {
                    String imp = ((JLabel)tp.pnlData.get(i).getComponent(0)).getText();
                    String proName = ((JLabel)tp.pnlData.get(i).getComponent(1)).getText();
                    outOfDateProduct = outOfDateProduct + imp +" - "+proName +"\n";
                }
            }
            ExpForm ep = new ExpForm(nearOutOfDateProduct,"Near Out of Date Product");
            ExpForm epx = new ExpForm(outOfDateProduct,"Out of Date Product");






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
