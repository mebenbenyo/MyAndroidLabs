package algonquin.cst2335.ayu00002;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ayu00002.R;
import algonquin.cst2335.ayu00002.data.ChatRoomViewModel;
import algonquin.cst2335.ayu00002.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ayu00002.databinding.ReceiveMessageBinding;
import algonquin.cst2335.ayu00002.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ArrayList<ChatMessage> messages;
    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;
    ChatRoomViewModel chatModel;
    ChatMessageDAO mDAO;
    MessageDatabase db;
    ChatMessage newMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();
        if (messages == null) {

            chatModel.messages.setValue(messages = new ArrayList<ChatMessage>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() );
                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter ));
            });
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder( binding.getRoot()); }
                else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder( binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.timeText.setText("");

                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                if (messages.get(position).isSentButton == true) {
                return 0; }
                else {
                return 1;
            }
            }
        });

        binding.sendButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh:mm a");
            String currentDateandTime = sdf.format(new Date());
            String messageInput = binding.textInput.getText().toString();
            boolean sender = true;
            newMessage = new ChatMessage(messageInput, currentDateandTime, sender);
            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            //clear the previous text
            binding.textInput.setText("");
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(new Runnable() {
                @Override
                public void run() {
                    newMessage.id = mDAO.insertMessage(newMessage);
                }
            });
        });

        binding.receiveButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh:mm a");
            String currentDateandTime = sdf.format(new Date());
            String messageInput = binding.textInput.getText().toString();
            boolean sender = false;
            newMessage = new ChatMessage(messageInput, currentDateandTime, sender);
            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            //clear the previous text
            binding.textInput.setText("");
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(new Runnable() {
                @Override
                public void run() {
                    newMessage.id = mDAO.insertMessage(newMessage);
                }
            });
        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

    }
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setTitle("Question:")
                        .setMessage("Do you want to delete the message: " + messageText.getText())
                        .setNegativeButton("No", (dialog, cl) -> { })
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            Executor thread = Executors.newSingleThreadExecutor();
                            ChatMessage m = messages.get(position);
                            thread.execute(() -> {
                                mDAO.deleteMessage(m);
                            });
                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            Snackbar.make(messageText, "You deleted message #"+position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", click ->{
                                      messages.add(position, m);
                                        runOnUiThread( () -> myAdapter.notifyItemInserted(position));
                                    })
                                    .show();
                        })
                                .create().show();
            });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}