package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.user.User;


public class LockUserRequest {

   private User user; private boolean lock;

    public LockUserRequest() {
    }

    public LockUserRequest(User user, boolean lock) {
        this.user = user;
        this.lock = lock;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
