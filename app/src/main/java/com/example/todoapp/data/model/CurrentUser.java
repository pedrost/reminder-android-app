package com.example.todoapp.data.model;

public class CurrentUser {
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        private String userId = "";

        private static final CurrentUser instance;

        static {
            instance = new CurrentUser();
        }

        public CurrentUser() {
        }

        public static CurrentUser getInstance() {
            return CurrentUser.instance;
        }
}
