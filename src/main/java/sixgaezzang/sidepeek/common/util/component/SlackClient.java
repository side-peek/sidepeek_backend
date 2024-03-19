package sixgaezzang.sidepeek.common.util.component;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.header;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.webhook.WebhookPayloads.payload;
import static sixgaezzang.sidepeek.common.util.CommonConstant.DOT_SEPARATOR;

import com.slack.api.Slack;
import com.slack.api.model.block.HeaderBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.element.ButtonElement;
import com.slack.api.model.block.element.ImageElement;
import io.sentry.protocol.SentryId;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import sixgaezzang.sidepeek.config.properties.SentryAlertProperties;
import sixgaezzang.sidepeek.config.properties.slack.SlackProperties;

@RequiredArgsConstructor
public class SlackClient {
    public static final String CANNOT_SEND_SLACK_MESSAGE = "Slack 메시지를 보낼 수 없습니다.";

    private final Slack slack;
    private final SlackProperties slackProperties;
    private final SentryAlertProperties sentryAlertProperties;

    public void sendErrorMessage(Exception exception, SentryId sentryId) {
        HeaderBlock headerBlock = header(header -> header
            .text(plainText("🚨 " + getExceptionClassName(exception) + " 감지")));

        SectionBlock messageSectionBlock = section(section -> section
            .text(markdownText("*Environment:* " + sentryAlertProperties.environment()
                + "\n*Message:* " + exception.getMessage()
                + "\n*Sentry Issue ID:* " + sentryId + "\n"))
            .accessory(ImageElement.builder()
                .imageUrl(slackProperties.getErrorImageUrl())
                .altText(slackProperties.getErrorImageAlt())
                .build()));

        SectionBlock linkButtonSectionBlock = section(section -> section
            .text(markdownText("*Sentry Issue* 보러가기 \uD83D\uDC49"))
            .accessory(ButtonElement.builder()
                .text(plainText("Click 👀"))
                .value("sentry_issue")
                .url(getSentryIssueUrl(sentryId))
                .actionId("button-action")
                .build()
            ));

        try {
            slack.send(slackProperties.url(), payload(p -> p
                .blocks(asBlocks(headerBlock, messageSectionBlock, linkButtonSectionBlock))
            ));
        } catch (IOException e) {
            throw new IllegalStateException(CANNOT_SEND_SLACK_MESSAGE);
        }
    }

    private String getExceptionClassName(Exception exception) {
        String exceptionRawName = exception.getClass().getName();
        int lastIndex = exceptionRawName.lastIndexOf(DOT_SEPARATOR);
        return exceptionRawName.substring(lastIndex + 1);
    }

    private String getSentryIssueUrl(SentryId sentryId) {
        return sentryAlertProperties.issueBaseUrl() + sentryId;
    }
}
