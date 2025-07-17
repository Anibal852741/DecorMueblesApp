package com.cardenas.stockifi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;




public class UsersActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private List<User> userList;
    private UsersAdapter usersAdapter;
    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        databaseManager = new DatabaseManager(this);
        userList = databaseManager.getAllUsers();

        RecyclerView recyclerView = findViewById(R.id.users_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(userList, user -> selectedUser = user);
        recyclerView.setAdapter(usersAdapter);

        Button addButton = findViewById(R.id.add_user_button);
        addButton.setOnClickListener(v -> showUserDialog("Agregar Usuario", null));

        Button editButton = findViewById(R.id.edit_user_button);
        editButton.setOnClickListener(v -> {
            if (selectedUser != null) {
                showUserDialog("Editar Usuario", selectedUser);
            } else {
                showToast("Selecciona un usuario para editar.");
            }
        });

        Button deleteButton = findViewById(R.id.delete_user_button);
        deleteButton.setOnClickListener(v -> {
            if (selectedUser != null) {
                showConfirmationDialog("Eliminar Usuario",
                        "¿Estás seguro de que deseas eliminar este usuario?",
                        () -> {
                            databaseManager.deleteUser(selectedUser.getId());
                            userList.remove(selectedUser);
                            selectedUser = null;
                            usersAdapter.notifyDataSetChanged();
                        });
            } else {
                showToast("Selecciona un usuario para eliminar.");
            }
        });
    }

    private void showUserDialog(String title, User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_user, null);
        builder.setView(dialogView);

        EditText nameInput = dialogView.findViewById(R.id.user_name_input);
        EditText emailInput = dialogView.findViewById(R.id.user_email_input);
        EditText passwordInput = dialogView.findViewById(R.id.user_password_input);
        Spinner roleSpinner = dialogView.findViewById(R.id.user_role_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        if (user != null) {
            nameInput.setText(user.getName());
            emailInput.setText(user.getEmail());
            passwordInput.setText(user.getPassword());
            roleSpinner.setSelection(user.getRole().equals("administrador") ? 0 : 1);
        }
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String role = roleSpinner.getSelectedItem().toString();
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast("Todos los campos son obligatorios.");
                return;
            }
            if (user != null) {
                // Actualizar usuario existente
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                user.setRole(role);
                databaseManager.updateUser(user.getId(), name, email, password, role);
            } else {
                // Agregar nuevo usuario
                databaseManager.insertUser(name, email, password, role);
                userList.clear();
                userList.addAll(databaseManager.getAllUsers());
            }
            selectedUser = null; // Desmarcar usuario seleccionado
            usersAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    private void showConfirmationDialog(String title, String message, Runnable onConfirm) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Sí", (dialog, which) -> onConfirm.run())
                .setNegativeButton("No", null)
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
