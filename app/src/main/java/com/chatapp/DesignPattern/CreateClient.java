package com.chatapp.DesignPattern;

import com.chatapp.Clients.Clients.AuthenticationClient;
import com.chatapp.Clients.Clients.ConnectionManagerClient;
import com.chatapp.Clients.Clients.GroupChannelClient;
import com.chatapp.Clients.Clients.UpdateUserDetailsClient;
import com.chatapp.Clients.Clients.UserListClient;
import com.chatapp.Clients.IClients.IAuthenticationClient;
import com.chatapp.Clients.IClients.IConnectionManagerClient;
import com.chatapp.Clients.IClients.IGroupChannelClient;
import com.chatapp.Clients.IClients.IUpdateUserDetailsClient;
import com.chatapp.Clients.IClients.IUserListClient;

public class CreateClient {

    IAuthenticationClient iAuthenticationClient = null;
    IConnectionManagerClient iConnectionManagerClient = null;
    IGroupChannelClient iGroupChannelClient = null;
    IUserListClient iUserListClient = null;
    IUpdateUserDetailsClient iUpdateUserDetailsClient = null;

    public IAuthenticationClient getAuthenticationClientInstance() {
        if (iAuthenticationClient == null)
            iAuthenticationClient = new AuthenticationClient();
        return iAuthenticationClient;
    }


    public IConnectionManagerClient getConnectionManagerClientInstance() {
        if (iConnectionManagerClient == null)
            iConnectionManagerClient = new ConnectionManagerClient();
        return iConnectionManagerClient;
    }

    public IGroupChannelClient getGroupChannelClientInstance() {
        if (iGroupChannelClient == null)
            iGroupChannelClient = new GroupChannelClient();
        return iGroupChannelClient;
    }

    public IUserListClient getUserListClientInstance() {
        if (iUserListClient == null)
            iUserListClient = new UserListClient();
        return iUserListClient;
    }

    public IUpdateUserDetailsClient getUpdateUserDetailsClient() {
        if (iUpdateUserDetailsClient == null)
            iUpdateUserDetailsClient = new UpdateUserDetailsClient();
        return iUpdateUserDetailsClient;
    }
}
