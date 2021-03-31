package com.example.internet_connection;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketIO {

    private static Socket _socket;

    public SocketIO() {
        // empty constructor
    }

    public SocketIO(String URL, String authenToken) {
        try {
            IO.Options opts = new IO.Options();
            opts.query = "token=" + authenToken;
            _socket = IO.socket(URL, opts);
            Boolean is_connected = establish_connection();
            if (!is_connected)
                throw new ArithmeticException("Error when create a socket with the server.");
        }
        catch (URISyntaxException e) {
            Log.e("Exception in SocketIO", e.toString());
        }
        catch (ArithmeticException e) {
            Log.e("Exception in SocketIO", e.toString());
        }
        catch (Exception e) {
            Log.e("Another exception", e.toString());
        }
    }

    public Socket getInstance() {
        return _socket;
    }

    public boolean establish_connection() {
        try {
            _socket.connect();
            return true;
        }
        catch (Exception e) {
            Log.e("Exeption", e.toString());
            return false;
        }
    }

    public boolean push_data(JSONObject data, String event_name) {
        try {
            _socket.emit(event_name, data);
            return true;
        } catch (Exception e) {
            Log.e("Exception in SocketIO", e.toString());
            return false;
        }
    }
}
