package com.zaze.demo.component.fileexplorer.view;

import com.zaze.aarrepo.commons.base.ZBaseView;
import com.zaze.demo.component.fileexplorer.adapter.FileEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-05-05 04:42 1.0
 */
public interface FileExplorerView extends ZBaseView {

    void showFileList(@NotNull List<FileEntity> fileList);
}
