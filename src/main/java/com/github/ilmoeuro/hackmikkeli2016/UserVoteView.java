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
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;

@RequiredArgsConstructor
public class
        UserVoteView
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
        Boolean vote;
    }

    @Getter
    private Plan plan;

    @Getter
    private PlanProposal proposal;

    @Getter
    @Setter
    private List<VotesRow> votes = new ArrayList<>();

    @Getter
    @Setter
    private boolean votesSaved;

    @Getter
    @Setter
    private String comment;

    @Getter
    private transient List<Plan> availablePlans;

    @Getter
    private transient List<PlanProposal> availableProposals;

    public void setPlan(Plan plan) {
        if (!Objects.equals(this.plan, plan)) {
            this.proposal = null;
            this.plan = plan;
            clearVotes();
            votesSaved = false;
        }
        refresh();
    }

    public void setProposal(PlanProposal proposal) {
        if (!Objects.equals(this.proposal, proposal)) {
            this.proposal = proposal;
            clearVotes();
            votesSaved = false;
        }
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
        });
    }

    public void saveVotes() {
        if (proposal == null) {
            return;
        }
        runner.exec(jooq -> {
            UnitOfWork uow = new UnitOfWork(jooq);
            int tileNumber = 0;
            for (VotesRow votesRow : votes) {
                for (VotesCell votesCell : votesRow.getCells()) {
                    if (votesCell.getVote()) {
                        PlanVote vote = new PlanVote(proposal, tileNumber);
                        uow.addEntity(vote);
                    }
                    tileNumber++;
                }
            }

            if (comment != null) {
                PlanComment entity = new PlanComment(proposal, comment);
                uow.addEntity(entity);
            }

            comment = null;

            uow.execute();
        });
        
        votesSaved = true;
    }

    private void clearVotes() {
        if (votes != null) {
            for (VotesRow row : votes) {
                for (VotesCell cell : row.getCells()) {
                    cell.setVote(null);
                }
                row.getCells().clear();
            }
            votes.clear();
        }

        if (plan == null ||proposal == null) {
            return;
        }

        for (int j=0; j<plan.getNumRows(); j++) {
            List<VotesCell> cells = new ArrayList<>();
            for (int i=0; i<plan.getNumColumns(); i++) {
                cells.add(new VotesCell(false));
            }
            votes.add(new VotesRow(cells));
        }
    }
}
