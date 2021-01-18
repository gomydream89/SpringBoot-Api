package kakaopay.kakaopaysec.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchMergeInDto {
    @NonNull
    @ApiModelProperty(example = "분당점")
    private String bfBrName;    //이전관리점명(폐점)
    @NonNull
    @ApiModelProperty(example = "판교점")
    private String toBrName;    //통합관리점명(이관)
}
