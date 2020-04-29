package cn.yat.modules.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/28
 * @Time: 14:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class UserEntity implements Serializable {

    private Long id;
    private String name;
    private String password;
    private String ipAdder;
}
