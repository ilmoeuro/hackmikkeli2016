/*
 * Copyright (C) 2015 Ilmo Euro <ilmo.euro@gmail.com>
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
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import lombok.Value;

@RequiredArgsConstructor
public final class UnitOfWork {

    private static @Value class PersistableRecord {
        UpdatableRecord updatableRecord;
        Persistable persistable;
    }
    
    private final DSLContext jooq;
    private final List<PersistableRecord> records = new ArrayList<>();

    public void addEntity(Persistable o) {
        if (o instanceof Plan) {
            addEntity(PLAN, o);
        } else if (o instanceof PlanProposal) {
            addEntity(PLAN_PROPOSAL, o);
        } else if (o instanceof PlanVote) {
            addEntity(PLAN_VOTE, o);
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "Argument %s is not a persistable entity",
                    o.toString()
                )
            );
        }
    }

    public void execute() {
        for (PersistableRecord record : records) {
            if (record.getPersistable().getPk() == null) {
                record.getUpdatableRecord().store();
                record.getPersistable().setPk(
                    record.getUpdatableRecord().getValue("pk", Integer.class));
            } else {
                record.getUpdatableRecord().update();
            }
        }
    }

    private <R extends UpdatableRecord> void addEntity(
        Table<R> table,
        Persistable val
    ) {
        R record = jooq.newRecord(table, val);
        records.add(new PersistableRecord(record, val));
    }
}
