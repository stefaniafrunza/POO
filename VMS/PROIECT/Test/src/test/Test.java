package test;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

public class Test {
    public static void main(String args[]) {

        File f1 = new File("campaigns.txt");
        File f2 = new File("users.txt");
        File f3 = new File("events.txt");

        int numberCampaigns = 0;
        int numberUsers = 0;
        int numberEvents = 0;

        LocalDateTime currentTime = null;

        BufferedReader bf = null;

        PrintWriter pw = null;

        LinkedList<Campaign> campaigns = new LinkedList<Campaign>();
        LinkedList<User> users = new LinkedList<User>();
        LinkedList<String> events = new LinkedList<String>();

        VMS vms = VMS.getInstance();

        try {
            bf = new BufferedReader(new FileReader(f1));
            String s = null;
            s = bf.readLine();
            numberCampaigns = Integer.parseInt(s);
            s = bf.readLine();
            StringTokenizer st1 = new StringTokenizer(s, " ");
            String s1 = st1.nextToken();
            String s2 = st1.nextToken();
            s1 = s1 + " " + s2;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime d1 = LocalDateTime.parse(s1, formatter);

            //adaug campaniile:

            while((s = bf.readLine()) != null) {
                StringTokenizer st2 = new StringTokenizer(s, ";");
                LinkedList<String> atoms = new LinkedList<String>();

                while(st2.hasMoreElements())
                    atoms.add(st2.nextToken());

                StringTokenizer st3 = new StringTokenizer(atoms.get(3), " ");
                String s3 = st3.nextToken();
                String s4 = st3.nextToken();
                s3 = s3 + " " + s4;
                LocalDateTime d2 = LocalDateTime.parse(s3, formatter); //startDate

                StringTokenizer st4 = new StringTokenizer(atoms.get(4), " ");
                String s5 = st4.nextToken();
                String s6 = st4.nextToken();
                s5 = s5 + " " + s6;
                LocalDateTime d3 = LocalDateTime.parse(s5, formatter); //endDate

                int idExtras = Integer.parseInt(atoms.get(0));
                int budgetExtras = Integer.parseInt(atoms.get(5));

                Campaign c = new Campaign(idExtras, atoms.get(1), atoms.get(2), d2, d3, budgetExtras, atoms.get(6), d1);
                campaigns.add(c);
            }

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bf.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        bf = null;

        try {
            bf = new BufferedReader(new FileReader(f2));
            String s = null;
            s = bf.readLine();
            numberUsers = Integer.parseInt(s);

            //de adaugat userii dupa ce instantiez clasa
            //adaug userii:

            while((s = bf.readLine()) != null) {
                StringTokenizer st5 = new StringTokenizer(s, ";");
                LinkedList<String> atoms = new LinkedList<String>();

                while(st5.hasMoreElements())
                    atoms.add(st5.nextToken());

                int idExtras = Integer.parseInt(atoms.get(0));
                User.UserType typeExtras = null;

                if(atoms.get(4).equals("ADMIN"))
                    typeExtras = User.UserType.ADMIN;
                else
                    typeExtras = User.UserType.GUEST;

                User u = new User(idExtras, atoms.get(1), atoms.get(3), atoms.get(2), typeExtras);
                users.add(u);
            }

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bf.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        bf = null;

        try {
            bf = new BufferedReader(new FileReader(f3));
            String s = null;
            //citesc prima linie, pe care se afla timpul:
            s = bf.readLine();
            StringTokenizer st2 = new StringTokenizer(s, " ");
            String s3 = st2.nextToken();
            String s4 = st2.nextToken();
            s3 = s3 + "T" + s4;
            currentTime = LocalDateTime.parse(s3);
            //citesc a doua linie, pe care se afla numarul de evenimente:
            s = bf.readLine();
            numberEvents = Integer.parseInt(s);

            while((s = bf.readLine()) != null)
                events.add(s);

            //pun fiecare linie in lista events, urmand apoi sa le despart in tokeni si sa
            //analizez fiecare eveniment posibil

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bf.close();
            } catch(IOException e) {
            e.printStackTrace();
            }
        }

        for(User u : users)
            vms.addUser(u);

        for(Campaign c : campaigns)
            vms.addCampaign(c);

        try {
            FileWriter fw = new FileWriter("output.txt");
            pw = new PrintWriter(fw);


            //luam pe rand din multimea de evenimente:
            for(int i = 0; i < events.size(); i++) {
                StringTokenizer st6 = new StringTokenizer(events.get(i), ";");
                LinkedList<String> atoms = new LinkedList<String>();

                while(st6.hasMoreElements())
                    atoms.add(st6.nextToken());
                //addCampaign:
                if(atoms.get(1).equals("addCampaign")) {
                    int userIdExtras = Integer.parseInt(atoms.get(0));

                    for(User u : users) {
                        if(u.id == userIdExtras) {
                            if(u.type.equals(User.UserType.ADMIN)) {
                                StringTokenizer st7 = new StringTokenizer(atoms.get(5), " ");
                                String s1 = st7.nextToken();
                                String s2 = st7.nextToken();
                                s1 = s1 + " " + s2;
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime ldt1 = LocalDateTime.parse(s1, formatter); //startDate

                                StringTokenizer st8 = new StringTokenizer(atoms.get(6), " ");
                                String s3 = st8.nextToken();
                                String s4 = st8.nextToken();
                                s3 = s3 + " " + s4;
                                LocalDateTime ldt2 = LocalDateTime.parse(s3, formatter); //endDate

                                int campaignIdExtras = Integer.parseInt(atoms.get(2));
                                int budgetExtras = Integer.parseInt(atoms.get(7));

                                Campaign c = new Campaign(campaignIdExtras, atoms.get(3),
                                atoms.get(4), ldt1, ldt2, budgetExtras, atoms.get(8),
                                currentTime);

                                vms.addCampaign(c);
                            }
                        }
                    }
                }

                //editCampaign:
                if(atoms.get(1).equals("editCampaign")) {
                    int userIdExtras = Integer.parseInt(atoms.get(0));

                    for(User u : users) {
                        if(u.id == userIdExtras) {
                            if(u.type.equals(User.UserType.ADMIN)) {
                                StringTokenizer st7 = new StringTokenizer(atoms.get(5), " ");
                                String s1 = st7.nextToken();
                                String s2 = st7.nextToken();
                                s1 = s1 + " " + s2 + ":00";
                                LocalDateTime ldt1 = LocalDateTime.parse(s1); //startDate

                                StringTokenizer st8 = new StringTokenizer(atoms.get(6), " ");
                                String s3 = st8.nextToken();
                                String s4 = st8.nextToken();
                                s3 = s3 + " " + s4 + ":00";
                                LocalDateTime ldt2 = LocalDateTime.parse(s3); //endDate

                                int campaignIdExtras = Integer.parseInt(atoms.get(2));
                                int budgetExtras = Integer.parseInt(atoms.get(7));

                                Campaign c = new Campaign(campaignIdExtras, atoms.get(3),
                                atoms.get(4), ldt1, ldt2, budgetExtras, "A", currentTime);
                                //Am pus strategia A, pentru ca oricum nu voi lucra cu strategii
                                //si oricum s-ar schimba eventual strategia doar in cazul
                                //campaniilor cu statusul NEW

                                vms.updateCampaign(campaignIdExtras, c, currentTime);
                            }
                        }
                    }
                }

                //cancelCampaign:
                if(atoms.get(1).equals("cancelCampaign")) {
                    int userIdExtras = Integer.parseInt(atoms.get(0));

                    for(User u : users) {
                        if(u.id == userIdExtras) {
                            if(u.type.equals(User.UserType.ADMIN)) {
                                int campaignIdExtras = Integer.parseInt(atoms.get(2));

                                vms.cancelCampaign(campaignIdExtras, currentTime);
                            }
                        }
                    }
                }

                //generateVoucher:
                if(atoms.get(1).equals("generateVoucher")) {
                    int userIdExtras = Integer.parseInt(atoms.get(0));

                    for(User u : users) {
                        if(u.id == userIdExtras) {
                            if(u.type.equals(User.UserType.ADMIN)) {
                                for(Campaign c : vms.campaigns) {
                                    int campaignIdExtras = Integer.parseInt(atoms.get(2));
                                    int valueExtras = Integer.parseInt(atoms.get(5));

                                    if(c.id == campaignIdExtras) {
                                        c.generateVoucher(atoms.get(3), atoms.get(4),
                                        valueExtras);
                                    }
                                }
                            }
                        }
                    }
                }

                //redeemVoucher:
                if(atoms.get(1).equals("redeemVoucher")) {
                    int userIdExtras = Integer.parseInt(atoms.get(0));

                    for(User u : users) {
                        if(u.id == userIdExtras) {
                            if(u.type.equals(User.UserType.ADMIN)) {
                                for(Campaign c : vms.campaigns) {
                                    int campaignIdExtras = Integer.parseInt(atoms.get(2));

                                    if(c.id == campaignIdExtras) {
                                        int voucherIdExtras = Integer.parseInt(atoms.get(3));
                                        StringTokenizer st9 = new StringTokenizer(atoms.get(4), " ");
                                        String s1 = st9. nextToken();
                                        String s2 = st9.nextToken();
                                        s1 = s1 + " " + s2;
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                        LocalDateTime ldt = LocalDateTime.parse(s1, formatter);
                                        c.redeemVoucher(voucherIdExtras, ldt);
                                    }
                                }
                            }
                        }
                    }
                }

                //getVouchers:
                if(atoms.get(1).equals("getVouchers")) {
                    int userIdExtras = Integer.parseInt(atoms.get(0));

                    for(User u : users) {
                        if(u.id == userIdExtras) {
                            if(u.type.equals(User.UserType.GUEST))
                                pw.println(u.vouchers.values());
                        }
                    }
                }

                //getObservers:
                if(atoms.get(1).equals("getObservers")) {
                    int userIdExtras = Integer.parseInt(atoms.get(0));

                    for(User u : users) {
                        if(u.id == userIdExtras) {
                            if(u.type.equals(User.UserType.ADMIN)) {
                                for(Campaign c : campaigns) { //din toate campnaiile
                                    int campaignIdExtras = Integer.parseInt(atoms.get(2));

                                    if(c.id == campaignIdExtras)
                                        pw.println(c.observers);
                                }
                            }
                        }
                    }
                }

                //getNotifications:
                if(atoms.get(1).equals("getNotifications")) {
                    int userIdExtras = Integer.parseInt(atoms.get(0));

                    for(User u : users) {
                        if(u.id == userIdExtras) {
                            if(u.type.equals(User.UserType.GUEST))
                                pw.println(u.notifications);
                        }
                    }
                }
                //getVoucher:
                if(atoms.get(1).equals("getVoucher")) {
                    int userIdExtras = Integer.parseInt(atoms.get(0));

                    for(User u : users) {
                        if(u.id == userIdExtras) {
                            if(u.type.equals(User.UserType.ADMIN)) {
                                for(Campaign c : campaigns) {
                                    int campaignIdExtras = Integer.parseInt(atoms.get(2));
                                    if(c.id == campaignIdExtras)
                                        pw.println(c.executeStrategy());
                                }
                            }
                        }
                    }
                }
            }

        } catch(FileNotFoundException e) {
            e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
            pw.close();
        }

    }
}