package pl.marcinek;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.*;
import java.sql.*;
import java.util.logging.Level;

import static pl.marcinek.sql.punkty;

public class Load {

    public static final TS3Config config = new TS3Config();
    public static String odp;
    public static String who;
    public static String amount;

    public static void main(String[] args) throws Exception{
        final TS3Config config = new TS3Config();
        config.setHost("localhost");

        final TS3Query query = new TS3Query(config);
        query.connect();

        final TS3Api api = query.getApi();
        api.login("querka", "F14DakiZ"); //localhost
        api.selectVirtualServerById(1);
        api.setNickname("Querka");
        System.out.println("Querka ON");
        config.setFloodRate(TS3Query.FloodRate.UNLIMITED);
        config.setDebugLevel(Level.ALL);
        api.setClientChannelGroup(2560, 19211,11604);

        api.registerAllEvents();

        final int clientId = api.whoAmI().getId();

        api.addTS3Listeners(new TS3Listener() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                if(e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String message = e.getMessage().toLowerCase();
                    if (message.equals("!pomoc")) {
                        api.sendPrivateMessage(e.getInvokerId(), "[b]――――――――――――[[color=red]System punktowy[/color] ]――――――――――――");
                        api.sendPrivateMessage(e.getInvokerId(), " ");
                        api.sendPrivateMessage(e.getInvokerId(), "[b][color=red]!add[/color] ( nick ) ( ilość punktów )  - Dodaje administratorowi punkty.");
                        api.sendPrivateMessage(e.getInvokerId(), "[b][color=red]!remove[/color] ( nick ) ( ilość punktów )  - Usuwa administratorowi punkty.");
                        api.sendPrivateMessage(e.getInvokerId(), "[b][color=red]!dodaj[/color] ( nick )  - Dodaje administratora.");
                        api.sendPrivateMessage(e.getInvokerId(), "[b][color=red]!usun[/color] ( nick )  - Usuwa administratora.");
                        api.sendPrivateMessage(e.getInvokerId(), "[b][b][color=red]!info[/color] ( nick )  - Wyświetla ilość punktów administratora.");
                        api.sendPrivateMessage(e.getInvokerId(), " ");
                        api.sendPrivateMessage(e.getInvokerId(), "[b]Coded [color=red]by[/color] Marcinek | [url]https://github.com/nick4nameyt[/url]");
                        api.sendPrivateMessage(e.getInvokerId(), " ");
                        api.sendPrivateMessage(e.getInvokerId(), "[b]―――――――――――――[ version [color=red]1.0[/color] ]―――――――――――――");
                    }
                }

                //dodaje punkty
                if(e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String message = e.getMessage().toLowerCase();
                    if(message.contains("!add")){ //!add <nick> <punkty>
                        odp = e.getMessage().substring(5);
                        who = odp.replaceAll("\\d","");

                        odp = e.getMessage().substring(5);
                        amount = odp.replaceAll("[^\\d.]", ""); // usuwa wszystkie gowna xd
                        api.sendChannelMessage("[b]Dodałem: " + who + amount + " [color=red]punktów[/color].");
                        try {
                            sql.addpkt();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                    }
                }

                //usuwa punkty
                if(e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String message = e.getMessage().toLowerCase();
                    if(message.contains("!remove")){ //!remove <nick> <ilosc>
                        odp = e.getMessage().substring(8);
                        who = odp.replaceAll("\\d","");

                        odp = e.getMessage().substring(8);
                        amount = odp.replaceAll("[^\\d.]", ""); // usuwa wszystkie gowna xd
                        api.sendChannelMessage("[b]Usunołem: " + who + amount + " [color=red]punktów[/color].");
                        try {
                            sql.removepkt();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                    }
                }

                //dodaje nowego admina do bazy dany
                if(e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String message = e.getMessage().toLowerCase();
                    if (message.contains("!dodaj")) { //!dodaj <nick>
                        api.sendChannelMessage("[b]Dodałem administratora: [color=red]" + e.getMessage().substring(7) + "[/color].");
                        odp = e.getMessage().substring(7);
                        try {
                            sql.adduser();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                    }
                }

                //usuwa administratora z bazy danych
                if(e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String message = e.getMessage().toLowerCase();
                    if (message.contains("!usun")) { //!usun <nick>
                        api.sendChannelMessage("[b]Usunołem administratora: [color=red]" + e.getMessage().substring(6) + "[/color].");
                        odp = e.getMessage().substring(6);
                        try {
                            sql.removeuser();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                    }
                }

                if(e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String message = e.getMessage().toLowerCase();
                    if (message.contains("!info")) { //!info <nick>
                        who = e.getMessage().substring(6);
                        try {
                            sql.showuser();
                            api.sendChannelMessage("[b][color=red]" + e.getMessage().substring(6) + "[/color] ma [color=red]" + punkty + "[/color] punktów.");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onClientJoin(ClientJoinEvent clientJoinEvent) {

            }

            @Override
            public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {

            }

            @Override
            public void onServerEdit(ServerEditedEvent serverEditedEvent) {

            }

            @Override
            public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

            }

            @Override
            public void onClientMoved(ClientMovedEvent clientMovedEvent) {

            }

            @Override
            public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {

            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {

            }

            @Override
            public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {

            }
        });
    }
}