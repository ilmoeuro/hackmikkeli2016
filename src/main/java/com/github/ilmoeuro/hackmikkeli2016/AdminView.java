/*
 * Copyright (C) 2016 Ilmo Euro <ilmo.euro@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.ilmoeuro.hackmikkeli2016;

import static com.github.ilmoeuro.hackmikkeli2016.schema.Tables.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class
        AdminView
implements
        Refreshable,
        Serializable
{
    private static final long serialVersionUID = 0l;
    private final SessionRunner runner;

    public static @Value class VotesRow implements Serializable {
        List<VotesCell> cells;
    }

    @AllArgsConstructor
    public static @Data class VotesCell implements Serializable {
        int numVotes;

        public String getStyle() {
            return String.format(
                    "background-color: rgba(0,255,0,%f);",
                    numVotes / 10.0);
        }
    }

    @Getter
    private Plan plan;

    @Getter
    private PlanProposal proposal;

    @Getter
    private transient List<VotesRow> votes = new ArrayList<>();

    @Getter
    private transient List<PlanComment> comments;

    @Getter
    private transient List<Plan> availablePlans;

    @Getter
    private transient List<PlanProposal> availableProposals;

    public void setPlan(Plan plan) {
        this.proposal = null;
        this.plan = plan;
        refresh();
    }

    public void setProposal(PlanProposal proposal) {
        this.proposal = proposal;
        refresh();
    }

    @Override
    public void refresh() {
        runner.exec(jooq -> {
            availablePlans = jooq
                    .select(PLAN.fields())
                    .from(PLAN)
                    .fetchInto(Plan.class);
            if (plan == null) {
                availableProposals = Collections.emptyList();
            } else {
                availableProposals = jooq
                        .select(PLAN_PROPOSAL.fields())
                        .from(PLAN_PROPOSAL)
                        .where(PLAN_PROPOSAL.PLAN_ID.eq(plan.getId()))
                        .fetchInto(PlanProposal.class);
            }

            votes = new ArrayList<>();
            int tileNumber = 0;
            if (plan != null && proposal != null) {
                for (int j=0; j<plan.getNumRows(); j++) {
                    List<VotesCell> cells = new ArrayList<>();
                    for (int i=0; i<plan.getNumColumns(); i++) {
                        int numVotes = jooq
                                .selectCount()
                                .from(PLAN_VOTE)
                                .where(
                                        PLAN_VOTE.PROPOSAL_ID.eq(proposal.getId())
                                        .and(PLAN_VOTE.TILE_NUMBER.eq(tileNumber)))
                                .fetchOneInto(Integer.class);
                        cells.add(new VotesCell(numVotes));
                        tileNumber++;
                    }
                    votes.add(new VotesRow(cells));
                }
            }

            comments = new ArrayList<>();

            if (plan != null && proposal != null) {
                comments = jooq
                        .select(PLAN_COMMENT.fields())
                        .from(PLAN_COMMENT)
                        .where(PLAN_COMMENT.PROPOSAL_ID.eq(proposal.getId())
                                .and(PLAN_COMMENT.DELETED.eq(false)))
                        .fetchInto(PlanComment.class);
            }
        });
    }
}
