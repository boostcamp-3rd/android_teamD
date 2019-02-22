package com.teamdonut.eatto.ui.board.detail;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.teamdonut.eatto.common.helper.RealmDataHelper;
import com.teamdonut.eatto.data.Board;
import com.teamdonut.eatto.data.Comment;
import com.teamdonut.eatto.data.model.comment.CommentRepository;
import io.reactivex.disposables.CompositeDisposable;

import java.util.List;

public class BoardDetailViewModel extends ViewModel {

    private CommentRepository commentRepository = CommentRepository.getInstance();
    private CompositeDisposable disposables = new CompositeDisposable();

    private final ObservableField<Board> mBoard = new ObservableField<>();
    private final MutableLiveData<List<Comment>> comments = new MutableLiveData<>();

    public void loadComments() {
        fetchComments();
    }

    private void fetchComments() {
        if (mBoard.get() != null) {
            disposables.add(commentRepository.getComments(data -> {
                if (data != null) {
                    comments.postValue(data);
                }
            }, mBoard.get().getId()));
        }
    }

    private void sendComments(Comment comment) {
        disposables.add(commentRepository.postComment(data -> {
        }, data -> {
            fetchComments();
        }, comment));
    }

    public void onWriteCommentClick(String inputText) {
        if (mBoard.get() != null) {
            Comment comment = new Comment(mBoard.get().getId(), RealmDataHelper.getUser().getNickName(), inputText);
            sendComments(comment);
        }
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }

    public MutableLiveData<List<Comment>> getComments() {
        return comments;
    }

    public ObservableField<Board> getBoard() {
        return mBoard;
    }
}
