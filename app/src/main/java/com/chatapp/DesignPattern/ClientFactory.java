package com.chatapp.DesignPattern;

import com.chatapp.Clients.IClients.IAuthenticationClient;
import com.chatapp.Clients.IClients.IConnectionManagerClient;
import com.chatapp.Clients.IClients.IGroupChannelClient;
import com.chatapp.Clients.IClients.IUpdateUserDetailsClient;
import com.chatapp.Clients.IClients.IUserListClient;

public class ClientFactory implements IClients {

    public static CreateClient createClient;

    public static IAuthenticationClient getAuthenticationClientInstance() {
        return getInstance(AuthenticationClient).iAuthenticationClient;
    }

    public static IConnectionManagerClient getConnectionManagerClientInstance() {
        return getInstance(ConnectionManagerClient).iConnectionManagerClient;
    }

    public static IGroupChannelClient getGroupChannelClientInstance() {
        return getInstance(GroupChannelClient).iGroupChannelClient;
    }

    public static IUserListClient getUserListClientInstance() {
        return getInstance(UserListClient).iUserListClient;
    }

    public static IUpdateUserDetailsClient getUpdateUserDetailsClientInstance() {
        return getInstance(UpdateUserDetailsClient).iUpdateUserDetailsClient;
    }

    public static CreateClient getInstance(int client_id) {
        if (createClient == null)
            createClient = new CreateClient();
        switch (client_id) {
            case AuthenticationClient:
                createClient.getAuthenticationClientInstance();
                break;
            case ConnectionManagerClient:
                createClient.getConnectionManagerClientInstance();
                break;
            case GroupChannelClient:
                createClient.getGroupChannelClientInstance();
                break;
            case UserListClient:
                createClient.getUserListClientInstance();
                break;
            case UpdateUserDetailsClient:
                createClient.getUpdateUserDetailsClient();
                break;
        }
        return createClient;
    }
}
