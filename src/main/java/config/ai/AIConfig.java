package config.ai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
//
//                    # 유의 사항 1
//                    약을 복용했는지 묻는 부분이 있을 텐데, 이 부분을 꼭 참고해서 response 시작 부분에 true인지 false인지 알려줘.
//                    복용했다면 true이고, 안 했다면 false야. 반드시 반드시 반드시 포함해야 해. true or false, 둘 중 하나만 포함하도록 해 무조건.
    private final String SUMMARY_PROMPT =
            """
                    # 요청
                    독거 노인 어르신께서 자기 일상을 AI와 대화로 나눈 <transcript>를 읽고 이 대화 내용을 요약해서 300자의 일기 형식으로 만들어 줘.
                    이 일기는 어르신의 자식분들이 보게 될 거야.

                    # 유의 사항 2
                    300자 일기 형식으로 요약하는 거야. 발화자는 어르신이셔. 발화자가 어르신처럼 보이도록 일기를 작성해.
                    발화자가 어르신이라는 걸 반드시 유의해서 점잖은 60~80대의 말투로 요약해야 해.
                                       
                    #유의 사항 3
                    일기 내용을 바탕으로 그림도 생성할 거니깐 그림이 다채롭게 나올 수 있도록 잘 요약해.
                    자식분들이 본다는 사실을 잊지 마.
                                      
                    # 유의 사항 4
                    오디오 외의 내용은 생성하지 마. 오디오 안의 내용만 가지고 요약해 줘.
                    
                    # 유의 사항 5
                   큰따옴표나 따옴표를 사용하지 말것
                    """;
//                    # 유의 사항 5
//                    위 유의사항들을 충족한 title을 중괄호로 감싸서 response시작부분에 유의사항1과 함께 제공해줘.(예시:{title:'노인회관에서 다른 할아버지들과 자녀 얘기를 나눈 날', boolean:true})
    private final String IMAGE_PROMPT =
            """
                    # 사전 입력 정보
                                        
                    ## 유의 사항
                    너가 어떤 행동을 했는지 또는 어떤 생각을 가지고 있는 지를 표현하는 것에 중점을 두었으면 좋겠어.
                    그림에는 텍스트가 포함되지 않아야해.
                    아래 <일기>의 내용을 바탕으로 그림을 그려줘
                    <일기>안에 있는 내용 만을 바탕으로 그림을 그려야 해
                    ## 이미지  스타일
                    한국의 따스한 그림 느낌으로 그려.
                                        
                    ## 주제
                    <일기>의 내용을 바탕으로 일기의 주제를 잘 파악해서 <일기>의 내용을 그림으로 그려줘
                    ## 페르소나
                    너에 대한 정보는 <사용자 정보>와 같아.
                    너는 <일기>내용을 보고 <일기>의 내용을 그림으로 표현하는 데 아주 뛰어난 감각이 있는 화가야.
                                        
                                        

                    <일기>
                    """;

    @Bean
    public String getImagePrompt() {
        return IMAGE_PROMPT;
    }

    @Bean
    public String getSummaryPrompt() {
        return SUMMARY_PROMPT;
    }
}