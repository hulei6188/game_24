package com.boot.web;

import com.boot.web.hulei.GameUtil;
import com.boot.web.hulei.Game_24;
import com.boot.web.hulei.Pocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@Controller
public class MainController {

    @Autowired
    private Game_24 game;

    @ResponseBody
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Response<String> add(@RequestBody Answer answer) {
        Response<String> response = new Response<>();


        if (!GameUtil.checkExpression(answer.getExpression())
                || !GameUtil.useNums(answer.getExpression(), game.getCardsPoint())) {
            response.setStatus(400);
            response.setMessage("The formula you give is illegal.");
            response.setResult(game.getResult());

            return response;
        }
        if (!game.judgeInput(answer.getExpression())) {
            response.setStatus(300);
            response.setMessage("The result of your equation is not 24.");
        } else {
            response.setMessage("you were right.");
            response.setStatus(200);
        }
        response.setResult(game.getResult());
        return response;


    }


    @ResponseBody
    @RequestMapping(value = "/store_pocket", method = RequestMethod.POST)
    public Response<String> addPockets(@RequestBody PocketList pocketList) {
        Response<String> response = new Response<>();

        if (!Game_24.check_pocket(pocketList.getNumOne()) || !Game_24.check_pocket(pocketList.getNumTwo())
                || !Game_24.check_pocket(pocketList.getNumThree()) || !Game_24.check_pocket(pocketList.getNumFour())) {
            response.setMessage("the point you give is not in range 1-13");
            response.setStatus(300);
            return response;
        }
        game.setCard_1(Pocket.values()[pocketList.getNumOne() - 1]);
        game.setCard_2(Pocket.values()[pocketList.getNumTwo() - 1]);
        game.setCard_3(Pocket.values()[pocketList.getNumThree() - 1]);
        game.setCard_4(Pocket.values()[pocketList.getNumFour() - 1]);
        if (game.getResult() == null) {
            response.setMessage("The poker group cannot make up 24 points.");
            response.setStatus(300);
        } else {
            response.setMessage("the pockets have been set.");
            response.setStatus(200);
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Response<String> getGameResult() {
        Response<String> response = new Response<>();
        response.setStatus(200);
        response.setResult(game.getResult());
        response.setMessage("ok");
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/get_pocket", method = RequestMethod.GET)
    public Response<Integer> getPocketPoint() {
        Response<Integer> response = new Response<>();
        response.setResult(game.getCardsPointList());
        response.setStatus(200);
        response.setMessage("ok");

        return response;
    }
}

