package onboard;

import java.util.List;

public interface IOnBoardCallBacks {

    void hideProgressBar();

    void showProgressBar();

    void showMessage(String message);

    void setAdapter(List<OnBoardResponseDTO> onBoardResponseDTOList);
}
