import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.*;
import java.lang.Thread;

class Main extends JFrame implements MouseListener {
    public static JSlider slider;
    public static int max_radius = 20;
    public static int max_bounds = 500;
    public static java.util.List<int[]> cord_list = new ArrayList<int[]>();
    public static java.util.List<int[]> init_cord_list = new ArrayList<int[]>();
    public static Graphics g;
    public static int round = 0;
    public static int timelimit = 10;
    public static int box_range = 50;
    public static int height;
    public static int width;
    public static boolean game_started = false;
    public static java.util.List<Color> colors = new ArrayList<Color>();
    public static int noc=3;

    public Main() {

    }

    public static boolean iscoll(int x, int y) {
        for (int i = 0; i < Main.cord_list.size(); i++) {
            int x2 = Main.cord_list.get(i)[0];
            int y2 = Main.cord_list.get(i)[1];
            double distance = Math.sqrt(((x2 - x) * (x2 - x)) + ((y2 - y) * (y2 - y)));
            if (distance < 2 * Main.max_radius) {
                return true;
            }
        }
        return false;
    }

    public static void updatecord(int lim) {
        for (int i = 0; i < Main.init_cord_list.size(); i++) {
            int x = init_cord_list.get(i)[0];
            int y = init_cord_list.get(i)[1];
            int x1;
            int y1;
            Random rand = new Random();
            do {
                x1 = (int) (rand.nextFloat() * (lim * 2)) + (x - lim);
                y1 = (int) (rand.nextFloat() * (lim * 2)) + (y - lim);
            } while (x1 < Main.max_radius || y1 < Main.max_radius || x1 > Main.width - Main.max_radius
                    || y1 > Main.height - Main.max_radius || iscoll(x1, y1));
            int[] cord = { x1, y1 };
            cord_list.set(i, cord);
        }
    }

    class DrawCircle extends JPanel {
        int no_of_circles;

        public DrawCircle(int no_of_circles) {
            this.no_of_circles = no_of_circles;
        }

        public void roundchange() {
            Main.timelimit = 10 - Main.round;
            Main.round++;
            Main.box_range += 15;
            System.out.println("round change" + Main.round);
        }

        public void new_circles() {
            Random rand = new Random();
            Main.width = getWidth();
            Main.height = getHeight();
            System.out.println(Main.width + " " + Main.height);
            Main.cord_list = new ArrayList<int[]>();
            for (int i = 0; i < Main.noc; i++) {
                int in_x;
                int in_y;
                do {
                    in_x = (int) (rand.nextFloat() * getWidth());
                    in_y = (int) (rand.nextFloat() * getHeight());
                } while (in_x < Main.max_radius || in_y < Main.max_radius || in_x > getWidth() - Main.max_radius
                        || in_x > getHeight() - Main.max_radius || iscoll(in_x, in_y));
                // g.drawOval(in_x, in_y, radius, radius);
                int[] cord = { in_x + Main.max_radius, in_y + Main.max_radius };
                Main.cord_list.add(cord);
                Main.init_cord_list.add(cord);
                Main.colors.add(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
            }
        }
        public void paintComponent(Graphics g) {
            int radius = Main.max_radius;
            for (int i = 0; i < Main.cord_list.size(); i++) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(Main.colors.get(i));
                g2d.fillOval((int) Main.cord_list.get(i)[0] - radius, (int) (Main.cord_list.get(i)[1] - radius),
                        radius * 2, radius * 2);
            }
        }
    }

    public static Main m;
    public static DrawCircle dc;

    public void stateChanged(ChangeEvent e) {
        System.out.println("value of Slider is =" + Main.slider.getValue());
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("mouse pressed at point:" + e.getX() + " " + e.getY());
        int clickedd = -1;
        for (int i = 0; i < Main.cord_list.size(); i++) {
            int x1 = e.getX();
            int y1 = e.getY() - Main.max_radius;
            int x2 = Main.cord_list.get(i)[0];
            int y2 = Main.cord_list.get(i)[1];
            double distance = Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
            if (distance <= Main.max_radius) {
                clickedd = i;
                System.out.println("distance=" + distance);
                break;
            }
        }
        if (clickedd != -1) {
            Main.cord_list.remove(clickedd);
            Main.init_cord_list.remove(clickedd);
        }
        Main.dc.repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public static void gamelogic() {

    }

    public static void main(String[] args) {
        JFrame frame_menu = new JFrame("menu");
        JLabel label = new JLabel();
        label.setText("This is a basic label");
        JPanel panel_m = new JPanel();
        panel_m.setLayout(new GridLayout(3, 1));
        panel_m.setBounds(10, 10, 200, 200);
        frame_menu.setVisible(true);
        frame_menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton start_B = new JButton("start game");
        JButton restart_B = new JButton("restart");
        Main.slider = new JSlider(3, 5, 3);
        Main.slider.setMinorTickSpacing(1);
        Main.slider.setMajorTickSpacing(1);
        Main.slider.setPaintTicks(true);
        Hashtable<Integer, JLabel> slider_labels = new Hashtable<>();
        slider_labels.put(3, new JLabel("Easy"));
        slider_labels.put(4, new JLabel("Medium"));
        slider_labels.put(5, new JLabel("Hard"));
        Main.slider.setLabelTable(slider_labels);

        Main.slider.setPaintLabels(true);
        panel_m.add(start_B);
        panel_m.add(restart_B);
        panel_m.add(Main.slider);
        frame_menu.add(panel_m);
        frame_menu.setSize(700, 300);
        JFrame frame_game = new JFrame("game");
        frame_game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_game.setSize(Main.max_bounds + Main.max_radius + 100, Main.max_bounds + Main.max_radius + 100);

        try {
            // start game
            start_B.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Main.noc=Main.slider.getValue();
                    System.out.println("noc"+Main.noc);
                    frame_menu.setVisible(false);
                    frame_game.setVisible(true);
                    Main.game_started = true;
                    Main.box_range = 50;
                    Main.timelimit = 10;
                    Main.m = new Main();
                    Main.dc = m.new DrawCircle(Main.noc);
                    //Main.dc.new_circles();
                    cord_list = new ArrayList<int[]>();
                    init_cord_list = new ArrayList<int[]>();
                    Main.dc.add(label);
                    frame_game.add(Main.dc);
                    frame_game.addMouseListener(m);
                }
            });
            // restart game
            restart_B.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Main.noc=Main.slider.getValue();
                    frame_menu.setVisible(false);
                    frame_game.setVisible(true);
                    Main.game_started = true;
                    //Main.dc.new_circles();
                    cord_list = new ArrayList<int[]>();
                    init_cord_list = new ArrayList<int[]>();
                    Main.round = 1;
                    Main.box_range = 50;
                    Main.timelimit = 10;
                }
            });
        } catch (Exception exc) {
            System.out.println("exception=" + exc.getMessage());
        }
        while (true) {
            for (int k = 0; k < 11; k++) {
                boolean time_ex = false;
                Main.timelimit = 10 - k;
                while (true) {
                    label.setText("round: " + Main.round + " time: " + Main.timelimit);
                    if (Main.round > 10 || Main.timelimit <= 0) {
                        if (Main.cord_list.size() > 0) {
                            time_ex = true;
                        }
                        break;
                    }
                    if (Main.cord_list.size() <= 0 && Main.game_started) {
                        Main.dc.roundchange();
                        Main.dc.new_circles();
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                        updatecord(Main.box_range);
                        if (Main.game_started) {
                            Main.timelimit--;
                            label.setText("round: " + Main.round + " time: " + Main.timelimit);
                        }
                    } catch (Exception ex) {
                        System.out.println("exception");
                    }
                    if (Main.dc != null) {
                        Main.dc.repaint();
                        label.setText("round: " + Main.round + " time: " + Main.timelimit);
                    }
                }
                if (Main.round > 10 || time_ex) {
                    Main.game_started = false;
                    System.out.println("gameover");
                    break;
                }
                System.out.println("nextround");
            }
            frame_menu.setVisible(true);
            frame_game.setVisible(false);
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println("end exception");
            }
        }

    }
}