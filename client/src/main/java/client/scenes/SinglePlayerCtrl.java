package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class SinglePlayerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public SinglePlayerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void test()
    {
        System.out.println("tres");
    }
}
