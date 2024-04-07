package com.example.mesdeputes;

import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeputyActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewName, textViewCirco, textViewEmail, textViewGroupe, textViewSite, textViewResponsibilities, textViewVotes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deputy);
        textViewName= findViewById(R.id.textViewDeputyName);
        textViewCirco= findViewById(R.id.textViewDeputyCirco);
        textViewGroupe= findViewById(R.id.textViewDeputyGroupe);
        textViewEmail= findViewById(R.id.textViewDeputyEmail);
        textViewSite = findViewById(R.id.textViewDeputySite);
        textViewResponsibilities = findViewById(R.id.textViewResponsabilitiesDetails);

        imageView= findViewById(R.id.imageViewDeputy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Deputy deputy= (Deputy) getIntent().getSerializableExtra("deputy");
        textViewName.setText(deputy.getFirstname()+" "+deputy.getLastname());
        textViewCirco.setText(deputy.getDepartment()+", "+
                deputy.getNameCirco()+ " "+ deputy.getNumCirco()+
                (deputy.getNumCirco()==1? "er": "eme")+" circoncription");
        textViewGroupe.setText(deputy.getGroupe());
        textViewEmail.setText(deputy.getEmail());
        textViewSite.setText(deputy.getSite());
        textViewVotes = findViewById(R.id.textViewVotesDetails);

        ApiServices.loadDeputyAvatar(this, deputy.getNameForAvatar(), imageView);


       //Displaying responsabilities in activity_deputy layout
        StringBuilder responsabilitiesDetails = new StringBuilder();
        //Limit votes display to 3
        int maxToShow = Math.min(deputy.getVotes().size(), 2);

        for (int i = 0; i < maxToShow; i++) {
            Responsability responsability = deputy.getResponsabilities().get(i);


            //Change date format to french format
            String originalDateStr = responsability.getDebutFunction();
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
            SimpleDateFormat desiredFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            String formattedDateStr;

            try {
                Date date = originalFormat.parse(originalDateStr);
                formattedDateStr = desiredFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                formattedDateStr = "Date inconnue";
            }



            responsabilitiesDetails
                    .append(responsability.getOrganism())
                    .append("<br><b>Fonction:</b> ").append(responsability.getFunction())
                    .append("<br><b>Depuis:</b> ").append(formattedDateStr)
                    .append("<br><br>");
        }

        if (responsabilitiesDetails.length() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textViewResponsibilities.setText(Html.fromHtml(responsabilitiesDetails.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                textViewResponsibilities.setText(Html.fromHtml(responsabilitiesDetails.toString()));
            }
        } else {
            textViewResponsibilities.setText("Aucune responsabilité enregistrée.");
        }

        //Displaying votes in activity_deputy layout
        StringBuilder votesDetails = new StringBuilder();

        //Limit votes display to 3
        int maxVotesToShow = Math.min(deputy.getVotes().size(), 3);


        for (int i = 0; i < maxVotesToShow; i++) {
            Vote vote = deputy.getVotes().get(i);

            //Condition to change color according to position value
            if(vote.getPosition().equalsIgnoreCase("pour")) {
                votesDetails.append("<font color='#006600'><b>").append(vote.getPosition()).append("</b></font> ")
                        .append(vote.getTitre())
                        .append("<br><br>");;
            } else if(vote.getPosition().equalsIgnoreCase("contre")) {
                votesDetails.append("<font color='red'><b>").append(vote.getPosition()).append("</b></font> ")
                        .append(vote.getTitre())
                        .append("<br><br>");;
            }

        }

        if (votesDetails.length() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textViewVotes.setText(Html.fromHtml(votesDetails.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                textViewVotes.setText(Html.fromHtml(votesDetails.toString()));
            }
        } else {
            textViewVotes.setText("Aucun vote enregistré.");
        }

        //HTML  Widget with statistics for each deputy

        /*WebView webViewDeputy = findViewById(R.id.webViewDeputy);
        // Nettoyage du prénom et du nom pour retirer les accents et les apostrophes
        String firstNameCleaned = deputy.getFirstname().toLowerCase().replaceAll("[éèêë]", "e").replaceAll("[àâä]", "a").replaceAll("[îï]", "i").replaceAll("[ôö]", "o").replaceAll("[ùûü]", "u").replaceAll("[ç]", "c").replaceAll("'", "");
        String lastNameCleaned = deputy.getLastname().toLowerCase().replaceAll("[éèêë]", "e").replaceAll("[àâä]", "a").replaceAll("[îï]", "i").replaceAll("[ôö]", "o").replaceAll("[ùûü]", "u").replaceAll("[ç]", "c").replaceAll("'", "");

        // Construction de l'URL avec le prénom et le nom nettoyés
        String iframeUrl = "//2012-2017.nosdeputes.fr/widget14/" + firstNameCleaned + "-" + lastNameCleaned + "?iframe=true&notags=1&nophoto=1&noactivite=1&width=550";
        String iframeHtml = "<!DOCTYPE html><html><body><iframe frameborder=\"0\" scrolling=\"no\" src=\"" + iframeUrl + "\" height=\"170\" width=\"500\"></iframe></body></html>";

        webViewDeputy.loadData(iframeHtml, "text/html; charset=utf-8", "UTF-8");*/

    }




    }


