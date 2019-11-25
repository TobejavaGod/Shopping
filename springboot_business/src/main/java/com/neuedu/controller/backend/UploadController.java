package com.neuedu.controller.backend;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author jyw
 * @date 2019/10/26-10:04
 */
@Controller
@RequestMapping("/manager")
@CrossOrigin(value = "http://localhost:8080")
public class UploadController {

    @Value("${business.imageHost}")
    private String imageHost;

    @GetMapping("/upload")
    public String upload(){
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public ServerResponse upload(@RequestParam("uploadfile")MultipartFile uploadfile){
        if(uploadfile==null || "".equals(uploadfile.getOriginalFilename())){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"必须上传图片");
        }
        // 获取上传的图片的名称
        String oldFileName = uploadfile.getOriginalFilename();
        // 获取文件扩展名
        String extendName = oldFileName.substring(oldFileName.lastIndexOf("."));
        // 生成新的文件名
        String newFileName = UUID.randomUUID().toString()+extendName;
        // 创建上传文件的目录
        File mkdir = new File("/usr/thundersoft/developer/upload");
        if(!mkdir.exists()){
            // 如果文件夹不存在就创建一个文件夹
            mkdir.mkdirs();
        }
        File newFile = new File(mkdir,newFileName);
        try {
            uploadfile.transferTo(newFile);
            ImageVO imageVO = new ImageVO(newFileName,imageHost+newFileName);
            return ServerResponse.serverResponseBySuccess(imageVO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.serverResponseByError();
    }
}
