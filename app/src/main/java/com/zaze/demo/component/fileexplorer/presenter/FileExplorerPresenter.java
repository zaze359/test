package com.zaze.demo.component.fileexplorer.presenter;

import com.zaze.demo.component.fileexplorer.FileEvent;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-05-05 04:42 1.0
 */
public interface FileExplorerPresenter {

    void loadFileList();

    void openFileOrDir(FileEvent fileEvent);

    void backToParent();
}
