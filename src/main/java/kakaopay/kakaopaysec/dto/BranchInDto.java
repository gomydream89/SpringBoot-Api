package kakaopay.kakaopaysec.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchInDto {
    @NonNull
    @ApiModelProperty(example = "판교점")
    private String brName;      //관리점명
}
